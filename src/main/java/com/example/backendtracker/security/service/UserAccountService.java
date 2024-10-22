package com.example.backendtracker.security.service;

import com.example.backendtracker.domain.models.UserAccount;
import com.example.backendtracker.domain.repositories.UserAccountRepository;
import com.example.backendtracker.security.dto.AuthenticationRequestDTO;
import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.exception.UserAlreadyExistsException;
import com.example.backendtracker.security.service.data.StudentExcelDto;
import com.example.backendtracker.security.service.data.UserStoringKeys;
import com.example.backendtracker.security.service.helper.UserServiceFactory;
import com.example.backendtracker.security.service.helper.student.StudentInitializer;
import com.example.backendtracker.security.util.JwtService;
import com.example.backendtracker.security.util.UserPasswordManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class UserAccountService {


    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;
    private final RoleService roleService;

    private final UserPasswordManager userPasswordManager;
    private final AuthenticationManager authenticationManager;
    private final JdbcTemplate jdbcTemplate;


    private final UserServiceFactory userServiceFactory;

    public String authenticateUser(AuthenticationRequestDTO authenticationRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.login(), authenticationRequest.password()));


        return obtainJwtToken(authentication);
    }

    @Transactional(rollbackFor = Exception.class)
    public void registerUser(UserRegistrationRequestDTO userRegistrationRequest) throws InvalidEncryptedDataException {

        checkUserExist(userRegistrationRequest);
        Integer idAccount = createUserAccount(userRegistrationRequest);
        initUserEntity(new UserStoringKeys(idAccount, userRegistrationRequest.key()), userRegistrationRequest.role());

    }

    private void initUserEntity(UserStoringKeys userStoringKeys, String role) throws InvalidEncryptedDataException {
        userServiceFactory.initUser(userStoringKeys, role);
    }

    private String obtainJwtToken(Authentication authentication) {
        final String role = authentication.getAuthorities().iterator().next().getAuthority();
        return jwtService.generateToken(authentication.getName(), role);

    }

    private void checkUserExist(UserRegistrationRequestDTO userRegistrationRequestDTO) {
        userAccountRepository.findByLogin(userRegistrationRequestDTO.login())
                .ifPresent(existingUser -> {
                    throw new UserAlreadyExistsException("User with login " + existingUser.getLogin() + " already exists.");
                });
    }

    public Integer createUserAccount(UserRegistrationRequestDTO userRegistrationRequest) {
        Integer roleId = roleService.getRoleIdByRoleName(userRegistrationRequest.role());
        UserAccount userAccount = new UserAccount(null,
                userRegistrationRequest.login(), userPasswordManager.encode(userRegistrationRequest.password()), roleId);

        return userAccountRepository.save(userAccount).getIdAccount();
    }

    public List<Integer> createUserAccountsInBatch(List<UserRegistrationRequestDTO> userRegistrationRequests) {
        String sql = "INSERT INTO useraccounts (login, password, id_role) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Use batchUpdate with a PreparedStatementCreator and BatchPreparedStatementSetter
        jdbcTemplate.batchUpdate(
                connection -> connection.prepareStatement(sql, new String[]{"id_account"}), // PreparedStatementCreator
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        UserRegistrationRequestDTO userRequest = userRegistrationRequests.get(i);
                        Integer roleId = roleService.getRoleIdByRoleName(userRequest.role());

                        ps.setString(1, userRequest.login());
                        ps.setString(2, userPasswordManager.encode(userRequest.password()));
                        ps.setInt(3, roleId);
                    }

                    @Override
                    public int getBatchSize() {
                        return userRegistrationRequests.size();
                    }
                },
                keyHolder // KeyHolder to capture generated keys
        );

        // Retrieve generated IDs
        List<Integer> generatedIds = keyHolder.getKeyList().stream()
                .map(keyMap -> (Integer) keyMap.get("id_account"))
                .collect(Collectors.toList());

        return generatedIds;
    }


}

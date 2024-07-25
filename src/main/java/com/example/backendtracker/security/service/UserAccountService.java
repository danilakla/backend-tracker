package com.example.backendtracker.security.service;

import com.example.backendtracker.domain.models.UserAccount;
import com.example.backendtracker.domain.repositories.UserAccountRepository;
import com.example.backendtracker.security.dto.AuthenticationRequestDTO;
import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import com.example.backendtracker.security.exception.UserAlreadyExistsException;
import com.example.backendtracker.security.service.data.UserStoringKeys;
import com.example.backendtracker.security.service.helper.UserServiceFactory;
import com.example.backendtracker.security.util.JwtService;
import com.example.backendtracker.security.util.UserPasswordManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class UserAccountService {


    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;
    private final RoleService roleService;

    private final UserPasswordManager userPasswordManager;
    private final AuthenticationManager authenticationManager;

    private final UserServiceFactory userServiceFactory;

    public String authenticateUser(AuthenticationRequestDTO authenticationRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.login(), authenticationRequest.password()));


        return obtainJwtToken(authentication);
    }


    public void registerUser(UserRegistrationRequestDTO userRegistrationRequest) {

        checkUserExist(userRegistrationRequest);
        Integer idAccount = createUserAccount(userRegistrationRequest);
        initUserEntity(new UserStoringKeys(idAccount, userRegistrationRequest.key()), userRegistrationRequest.role());

    }

    private void initUserEntity(UserStoringKeys userStoringKeys, String role) {
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

    private Integer createUserAccount(UserRegistrationRequestDTO userRegistrationRequest) {
        Integer roleId = roleService.getRoleIdByRoleName(userRegistrationRequest.role());
        UserAccount userAccount = new UserAccount(null,
                userRegistrationRequest.login(), userPasswordManager.encode(userRegistrationRequest.password()), roleId);

        return  userAccountRepository.save(userAccount).getIdAccount();
    }


}

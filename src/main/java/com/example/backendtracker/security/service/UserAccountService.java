package com.example.backendtracker.security.service;

import com.example.backendtracker.domain.repositories.UserAccountRepository;
import com.example.backendtracker.domain.repositories.UserRoleRepository;
import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//TODO REVIEW AFTER INIT ENTITY FOR ALL PROJECT

@Service
public class UserAccountService {

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository, UserRoleRepository userRoleRepository, UserPasswordManager userPasswordManager) {
        this.userAccountRepository = userAccountRepository;
        this.userRoleRepository = userRoleRepository;
        this.userPasswordManager = userPasswordManager;
    }
    private final UserAccountRepository userAccountRepository;
    private final UserRoleRepository userRoleRepository;

    private final UserPasswordManager userPasswordManager;




    public void registerUser(UserRegistrationRequestDTO userRegistrationRequest, String sql) {
        String encryptedPassword = userPasswordManager.encode(userRegistrationRequest.password());



//        userAccountRepository.findByLogin(userRegistrationRequest.login()).stream().findFirst().orElseThrow(e->new );
//        User user = userService.createNewUser(registrationUserDto);


    }


}

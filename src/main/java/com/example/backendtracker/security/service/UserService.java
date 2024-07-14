package com.example.backendtracker.security.service;

import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//TODO REVIEW AFTER INIT ENTITY FOR ALL PROJECT

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void registerUser(UserRegistrationRequestDTO userRegistrationRequest, String sql) {
        String encryptedPassword = bCryptPasswordEncoder.encode(userRegistrationRequest.password());
        jdbcTemplate.update(sql,userRegistrationRequest.id(), userRegistrationRequest.username(), encryptedPassword);


    }
}

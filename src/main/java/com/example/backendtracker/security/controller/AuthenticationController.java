package com.example.backendtracker.security.controller;

import com.example.backendtracker.security.dto.AuthenticationRequestDTO;
import com.example.backendtracker.security.dto.AuthenticationResponseDTO;
import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.util.JwtService;
import com.example.backendtracker.security.service.UserAccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class AuthenticationController {


    private final UserAccountService userService;

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthenticationResponseDTO> createAuthenticationToken(@RequestBody AuthenticationRequestDTO authenticationRequest) throws Exception {

        String jwtAccessToken = userService.authenticateUser(authenticationRequest);
        return ResponseEntity.ok(new AuthenticationResponseDTO(jwtAccessToken));
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequestDTO userRegistrationRequest) throws InvalidEncryptedDataException {
        userService.registerUser(userRegistrationRequest);
        return ResponseEntity.status(201).build();
    }


    @GetMapping("/student")
    public String student() {
        return "ok student";
    }


    @GetMapping("/teacher")
    public String teacher() {
        return "ok teacher";
    }
}

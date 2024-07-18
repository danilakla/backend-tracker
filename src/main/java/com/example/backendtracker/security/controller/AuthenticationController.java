package com.example.backendtracker.security.controller;

import com.example.backendtracker.security.dto.AuthenticationRequestDTO;
import com.example.backendtracker.security.dto.AuthenticationResponseDTO;
import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import com.example.backendtracker.security.service.JwtUtil;
import com.example.backendtracker.security.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
//TODO REVIEW AFTER INIT ENTITY FOR ALL PROJECT

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserAccountService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequestDTO authenticationRequest) throws Exception {
        System.out.println(123);
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.username(), authenticationRequest.password())
            );

        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final String role = authentication.getAuthorities().iterator().next().getAuthority();
        final String jwt = jwtUtil.generateToken( authentication.getName(), role);

        return ResponseEntity.ok(new AuthenticationResponseDTO(jwt));
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequestDTO userRegistrationRequest) {
        try {
            if (Objects.equals(userRegistrationRequest.role(), "Teacher")) {
                userService.registerUser(userRegistrationRequest, "INSERT INTO t_user_teacher (id, c_username, c_password) VALUES (?, ?, ?)");

            } else if (Objects.equals(userRegistrationRequest.role(), "Student")) {
                userService.registerUser(userRegistrationRequest, "INSERT INTO t_user_student (id, c_username, c_password) VALUES ( ?, ?, ?)");

            }
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

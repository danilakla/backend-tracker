package com.example.backendtracker.security.dto;

public record UserRegistrationRequestDTO(String username, String password, String role, Integer id) {

}
package com.example.backendtracker.security.dto;

import jakarta.annotation.Nullable;

public record UserRegistrationRequestDTO(String login, String password, String role, @Nullable String key) {

}
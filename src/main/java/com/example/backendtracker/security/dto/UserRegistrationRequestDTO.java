package com.example.backendtracker.security.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record UserRegistrationRequestDTO(String login, String password, String role, String name, String lastname,
                                         String surname, @Nullable String key) {

}
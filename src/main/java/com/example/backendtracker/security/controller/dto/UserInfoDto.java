package com.example.backendtracker.security.controller.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfoDto {
    String login;
    String role;
    String name;
    String lastname;
    String surname;
}

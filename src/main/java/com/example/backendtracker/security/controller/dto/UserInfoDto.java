package com.example.backendtracker.security.controller.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfoDto {
    Integer idAccount;
    String login;
    String role;
    String name;
    String lastname;
    String surname;
}

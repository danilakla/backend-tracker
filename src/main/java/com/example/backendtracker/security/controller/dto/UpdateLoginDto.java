package com.example.backendtracker.security.controller.dto;

import lombok.Builder;

@Builder
public record UpdateLoginDto(UserInfoDto userInfoDto, String jwt) {
}

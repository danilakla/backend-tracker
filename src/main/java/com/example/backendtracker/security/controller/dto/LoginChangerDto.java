package com.example.backendtracker.security.controller.dto;

import lombok.Builder;
import lombok.Data;

@Builder
public record LoginChangerDto(String newLogin, String role) {
}

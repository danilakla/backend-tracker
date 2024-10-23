package com.example.backendtracker.security.service.helper.student.dto;

import lombok.Builder;

@Builder
public record StudentResultDto(String name,
                               String lastname,
                               String surname,
                               String numberOfGroup,
                               String specialty,
                               String login,
                               String password,
                               String parentKey) {
}

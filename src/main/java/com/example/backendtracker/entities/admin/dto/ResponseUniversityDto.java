package com.example.backendtracker.entities.admin.dto;

import lombok.Builder;

@Builder
public record ResponseUniversityDto(String name, String description, Integer idUniversity) {
}

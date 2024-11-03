package com.example.backendtracker.entities.dean.dto;

import lombok.Builder;

@Builder
public record UpdateSubjectDto(String name, String description, Integer id) {
}

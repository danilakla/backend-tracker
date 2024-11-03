package com.example.backendtracker.entities.dean.dto;

import lombok.Builder;

@Builder
public record CreateSubjectDto(String name, String description) {
}

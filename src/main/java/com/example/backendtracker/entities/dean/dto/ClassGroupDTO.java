package com.example.backendtracker.entities.dean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassGroupDTO {
    private Integer id;
    private String description;
    private Long idSubject;
    private Long idClassFormat;
    private Long idTeacher;

    // Getters and Setters
}
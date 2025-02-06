package com.example.backendtracker.entities.teacher.dto;

import lombok.Builder;

@Builder
public record UpdateStudentGrade(
        Integer idStudentGrate,
        Integer grade,
        String description,
        Boolean isPassLab,
        Integer attendance
) {
}

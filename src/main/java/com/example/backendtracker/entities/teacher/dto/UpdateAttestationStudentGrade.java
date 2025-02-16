package com.example.backendtracker.entities.teacher.dto;

import lombok.Builder;

@Builder
public record UpdateAttestationStudentGrade(
        Integer idAttestationStudentGrades,
        Double avgGrade,
        Double hour,
        Integer currentCountLab,
        Integer maxCountLab,
        Boolean isAttested
) {
}

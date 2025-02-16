package com.example.backendtracker.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("attestationstudentgrades")
public class AttestationStudentGrade {
    @Id
    private Integer idAttestationStudentGrades;
    private Integer idStudent;
    private Integer idClass;
    private Double avgGrade;
    private Double hour;
    private Integer currentCountLab;
    private Integer maxCountLab;
    private Boolean isAttested;
}

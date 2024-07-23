package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("studentgrades")
public class StudentGrade {
    @Id
    private Integer idStudent;
    private Integer idClass;
    private Integer grade;
    private String description;
    private Boolean attendance;
}

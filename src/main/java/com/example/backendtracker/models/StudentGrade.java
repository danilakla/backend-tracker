package com.example.backendtracker.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("StudentGrades")
public class StudentGrade {
    @Id
    private Integer idStudent;
    private Integer idClass;
    private Integer grade;
    private String description;
    private Boolean attendance;
}

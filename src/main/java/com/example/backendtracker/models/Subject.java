package com.example.backendtracker.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("Subjects")
public class Subject {
    @Id
    private Integer idSubject;
    private Integer idDiscipline;
    private Integer idTeacher;
}

package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("teachers")
public class Teacher {
    @Id
    private Integer idTeacher;
    private Integer idUniversity;
    private String lastName;
    private String firstName;
    private String patronymic;
    private Integer idAccount;
}

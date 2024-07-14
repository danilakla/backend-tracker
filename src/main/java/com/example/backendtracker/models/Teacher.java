package com.example.backendtracker.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("Teachers")
public class Teacher {
    @Id
    private Integer idTeacher;
    private Integer idUniversity;
    private String lastName;
    private String firstName;
    private String patronymic;
}

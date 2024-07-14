package com.example.backendtracker.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("Students")
public class Student {
    @Id
    private Integer idStudent;
    private Integer idSubgroup;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String login;
    private String password;
    private String keyStudentParents;
}

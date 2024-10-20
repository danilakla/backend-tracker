package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("students")
public class Student {
    @Id
    private Integer idStudent;
    private Integer idSubgroup;
    private String flpName;
    private String login;
    private String password;
    private String keyStudentParents;
    private Integer idAccount;
}

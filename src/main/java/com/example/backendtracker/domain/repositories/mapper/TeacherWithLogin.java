package com.example.backendtracker.domain.repositories.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TeacherWithLogin {
    private Integer idTeacher;
    private Integer idUniversity;
    private String flpName;
    private Integer idAccount;
    private String login;

    // Constructor

}
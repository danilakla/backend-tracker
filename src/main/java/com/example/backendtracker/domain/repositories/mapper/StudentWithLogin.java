package com.example.backendtracker.domain.repositories.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class StudentWithLogin {
    private Integer idStudent;
    private String flpName;
    private String keyStudentParents;
    private String login;
    private Integer idSubgroup;
    private Integer idAccount;


}
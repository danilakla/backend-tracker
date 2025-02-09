package com.example.backendtracker.domain.repositories.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DeanWithLogin {
    private Integer idDean;
    private String flpName;
    private String faculty;
    private Integer idUniversity;
    private Integer idAccount;
    private String login;

    // Constructor

}
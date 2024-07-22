package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("Deans")
public class Dean {
    @Id
    private Integer idDean;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String faculty;
    private Integer idUniversity;
    private Integer idAccount;
}
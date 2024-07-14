package com.example.backendtracker.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
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
}
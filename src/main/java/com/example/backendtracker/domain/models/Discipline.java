package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("disciplines")
public class Discipline {
    @Id
    private Integer idDiscipline;
    private Integer idDean;
    private String name;
}

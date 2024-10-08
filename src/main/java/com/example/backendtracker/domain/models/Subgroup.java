package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("subgroups")
public class Subgroup {
    @Id
    private Integer idSubgroup;
    private String subgroupNumber;
    private LocalDate admissionDate;
    private Integer idDean;
    private Integer idSpecialty;
}

package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("Specialties")
public class Specialty {
    @Id
    private Integer idSpecialty;
    private String name;
    private Integer idDean;
}

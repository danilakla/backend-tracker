package com.example.backendtracker.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
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

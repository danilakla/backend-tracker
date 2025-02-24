package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("classes")
public class Classes {
    @Id
    private Integer idClass;
    private Integer idClassHold;

    private LocalDate dateCreation;
    private Boolean isAttestation;
    private String className;


}

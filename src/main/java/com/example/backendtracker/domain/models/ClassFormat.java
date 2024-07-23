package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("classformats")
public class ClassFormat {
    @Id
    private Integer idClassFormat;
    private String formatName;
    private String description;
    private Integer idDean;
}
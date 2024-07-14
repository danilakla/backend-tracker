package com.example.backendtracker.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("ClassFormats")
public class ClassFormat {
    @Id
    private Integer idClassFormat;
    private String formatName;
    private String description;
    private Integer idDean;
}
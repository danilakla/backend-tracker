package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("Universities")
public class University {
    @Id
    private Integer idUniversity;
    private String name;
    private String description;
    private Integer idAdmin;
}
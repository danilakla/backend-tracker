package com.example.backendtracker.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
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
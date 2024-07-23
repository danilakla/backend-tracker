package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("classgroups")
public class ClassGroup {
    @Id
    private Integer idClassGroup;
    private Integer idSubject;
    private String description;
    private Integer idClassFormat;
    private Integer idTeacher;
}

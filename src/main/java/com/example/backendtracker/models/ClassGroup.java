package com.example.backendtracker.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("ClassGroups")
public class ClassGroup {
    @Id
    private Integer idClassGroup;
    private Integer idSubject;
    private String description;
    private Integer quantity;
    private Integer idClassFormat;
    private Integer idTeacher;
}

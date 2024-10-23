package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("teachers")
public class Teacher {
    @Id
    private Integer idTeacher;
    private String flpName;
    private Integer idAccount;
    private Integer idUniversity;
}

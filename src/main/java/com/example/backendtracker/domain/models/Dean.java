package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("deans")
public class Dean {
    @Id
    private Integer idDean;
    private String flpName;
    private String faculty;
    private Integer idUniversity;
    private Integer idAccount;
}
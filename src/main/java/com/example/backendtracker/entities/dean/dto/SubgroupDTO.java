
package com.example.backendtracker.entities.dean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubgroupDTO {
    private Integer id;
    private String subgroupNumber;
    private LocalDate admissionDate;
}
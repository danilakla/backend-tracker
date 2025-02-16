package com.example.backendtracker.entities.dean.dto;

import lombok.*;

import java.util.List;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupedResultDTO {
    private SubgroupDTO subgroup;
    private List<StudentDTO> students;

    // Getters and Setters
}
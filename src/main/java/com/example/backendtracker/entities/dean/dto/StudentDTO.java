package com.example.backendtracker.entities.dean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private Integer id;
    private String name;
    private SubgroupDTO subgroup;
    private List<ClassGroupDTO> classGroups = new ArrayList<>(); // Initialize here
    private Integer unattestedCount=0;

    // Getters and Setters
}
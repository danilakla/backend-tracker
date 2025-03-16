package com.example.backendtracker.entities.dean.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SubgroupWithClassGroups {
    private Long idSubgroup;
    private String subgroupNumber;
    private LocalDate admissionDate;
    private List<ClassGroupInfoDean> classGroups;
}

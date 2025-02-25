package com.example.backendtracker.entities.dean.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubgroupWithClassGroups {
    private Long idSubgroup;
    private String subgroupNumber;
    private List<ClassGroupInfoDean> classGroups;
}

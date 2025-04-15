package com.example.backendtracker.entities.dean.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ReassignStudentsToNewGroup {
    private Set<Integer> studentsId;
    private Integer subgroupId;
}
package com.example.backendtracker.entities.dean.dto;

import lombok.Data;

@Data
public class ClassGroupInfoDean {
    private Long idClassGroup;
    private Long idClassHold;
    private String description;
    private String subjectName;
    private String formatName;
    private String teacherName;
}
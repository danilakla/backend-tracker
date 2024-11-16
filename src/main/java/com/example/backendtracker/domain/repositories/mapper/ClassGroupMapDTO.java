package com.example.backendtracker.domain.repositories.mapper;

import lombok.Builder;
import lombok.Data;

@Data
public class ClassGroupMapDTO {
    private int idClassGroup;
    private String description;
    private String subjectName;
    private String formatName;
    private String teacherName;

}

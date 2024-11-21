package com.example.backendtracker.domain.repositories.mapper;

import lombok.Data;

@Data
public class ClassGroupMapForStudentDTO {
    private int idClassGroup;
    private int idClassGroupToSubgroup;
    private int idSubgroup;
    private String description;
    private String subjectName;
    private String formatName;
    private String teacherName;

}

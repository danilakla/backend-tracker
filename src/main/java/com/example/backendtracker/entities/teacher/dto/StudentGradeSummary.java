package com.example.backendtracker.entities.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentGradeSummary {
    private int studentId;
    private Double avgGrade;
    private int attendanceCount;
    private int passLabCount;


}
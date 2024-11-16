package com.example.backendtracker.entities.teacher.dto;

import com.example.backendtracker.domain.models.Classes;
import com.example.backendtracker.domain.models.Student;
import com.example.backendtracker.domain.models.StudentGrade;
import lombok.Builder;

import java.util.List;

@Builder
public record TableViewDto(List<Student> students, List<Classes> classes, List<StudentGrade> studentGrades) {
}

package com.example.backendtracker.entities.teacher.dto;

import com.example.backendtracker.domain.models.Classes;
import com.example.backendtracker.domain.models.StudentGrade;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
public record StudentAttendanceDto(Integer studentGrateId,
                                   Integer attendanceCode) {
}

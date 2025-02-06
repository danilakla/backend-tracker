package com.example.backendtracker.domain.repositories.mapper;

import com.example.backendtracker.entities.teacher.dto.StudentGradeSummary;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentGradeSummaryRowMapper implements RowMapper<StudentGradeSummary> {

    @Override
    public StudentGradeSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
        int studentId = rs.getInt("studentId");
        Double avgGrade = rs.getDouble("avgGrade");
        int attendanceCount = rs.getInt("attendanceCount");
        int passLabCount = rs.getInt("passLabCount");

        return new StudentGradeSummary(studentId, avgGrade, attendanceCount, passLabCount);
    }
}
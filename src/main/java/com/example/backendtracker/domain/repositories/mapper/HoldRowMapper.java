package com.example.backendtracker.domain.repositories.mapper;

import com.example.backendtracker.entities.teacher.dto.StudentGradeSummary;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HoldRowMapper implements RowMapper<HoldStudentProjection> {

    @Override
    public HoldStudentProjection mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer holdId = rs.getInt("hold_id");
        Integer studentId = rs.getInt("student_id");

        return new HoldStudentProjection(studentId, holdId);
    }
}
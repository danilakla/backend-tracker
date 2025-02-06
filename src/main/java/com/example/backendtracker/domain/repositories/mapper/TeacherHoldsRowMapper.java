package com.example.backendtracker.domain.repositories.mapper;

import com.example.backendtracker.entities.admin.dto.TeacherHolds;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class TeacherHoldsRowMapper implements RowMapper<TeacherHolds> {

    @Override
    public TeacherHolds mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TeacherHolds(
                rs.getInt("teacher_id"),
                Arrays.asList((Integer[]) rs.getArray("hold_ids").getArray())
        );
    }
}
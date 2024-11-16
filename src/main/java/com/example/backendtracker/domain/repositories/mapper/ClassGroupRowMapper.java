package com.example.backendtracker.domain.repositories.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassGroupRowMapper implements RowMapper<ClassGroupMapDTO> {
    @Override
    public ClassGroupMapDTO mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        ClassGroupMapDTO classGroup = new ClassGroupMapDTO();
        classGroup.setIdClassGroup(resultSet.getInt("id_class_group"));
        classGroup.setDescription(resultSet.getString("description"));
        classGroup.setSubjectName(resultSet.getString("name"));
        classGroup.setFormatName(resultSet.getString("format_name"));
        classGroup.setTeacherName(resultSet.getString("flp_name"));
        return classGroup;
    }
}

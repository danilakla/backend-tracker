package com.example.backendtracker.domain.repositories.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassGroupForStudentRowMapper implements RowMapper<ClassGroupMapForStudentDTO> {
    @Override
    public ClassGroupMapForStudentDTO mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        ClassGroupMapForStudentDTO classGroup = new ClassGroupMapForStudentDTO();
        classGroup.setIdClassGroup(resultSet.getInt("idClassGroup"));
        classGroup.setIdClassGroupToSubgroup(resultSet.getInt("idClassGroupToSubgroup"));
        classGroup.setIdSubgroup(resultSet.getInt("idSubgroup"));
        classGroup.setIdHold(resultSet.getInt("idHold"));
        classGroup.setDescription(resultSet.getString("description"));
        classGroup.setSubjectName(resultSet.getString("subjectName"));
        classGroup.setFormatName(resultSet.getString("formatName"));
        classGroup.setTeacherName(resultSet.getString("teacherName"));
        return classGroup;
    }
}

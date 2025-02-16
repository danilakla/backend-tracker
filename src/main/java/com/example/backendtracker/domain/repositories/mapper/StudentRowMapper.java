package com.example.backendtracker.domain.repositories.mapper;

import com.example.backendtracker.entities.dean.dto.*;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentRowMapper implements RowMapper<StudentDTO> {
    @Override
    public StudentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Map subgroup information
        SubgroupDTO subgroup = new SubgroupDTO();
        subgroup.setId(rs.getInt("id_subgroup"));
        subgroup.setSubgroupNumber(rs.getString("subgroup_number"));
        subgroup.setAdmissionDate(rs.getDate("admission_date").toLocalDate());

        // Map student information
        StudentDTO student = new StudentDTO();
        student.setId(rs.getInt("id_student"));
        student.setName(rs.getString("student_name"));
        student.setSubgroup(subgroup);

        // Map class group information
        ClassGroupDTO classGroup = new ClassGroupDTO();
        classGroup.setId(rs.getInt("id_class_group"));
        classGroup.setDescription(rs.getString("class_group_description"));
        classGroup.setIdSubject(rs.getLong("id_subject"));
        classGroup.setIdClassFormat(rs.getLong("id_class_format"));
        classGroup.setIdTeacher(rs.getLong("id_teacher"));

        // Initialize classGroups list if null
        if (student.getClassGroups() == null) {
            student.setClassGroups(new ArrayList<>());
        }

        // Add class group to student
        student.getClassGroups().add(classGroup);

        return student;
    }
}
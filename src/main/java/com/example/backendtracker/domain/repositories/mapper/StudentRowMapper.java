package com.example.backendtracker.domain.repositories.mapper;

import com.example.backendtracker.entities.dean.dto.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentRowMapper implements RowMapper<StudentDTO> {
    @Override
    public StudentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Map subgroup
        SubgroupDTO subgroup = new SubgroupDTO();
        subgroup.setId(rs.getInt("id_subgroup"));
        subgroup.setSubgroupNumber(rs.getString("subgroup_number"));
        subgroup.setAdmissionDate(rs.getDate("admission_date").toLocalDate());

        // Map student
        StudentDTO student = new StudentDTO();
        student.setId(rs.getInt("id_student"));
        student.setName(rs.getString("student_name"));
        student.setUnattestedCount(rs.getInt("unattested_count"));
        student.setSubgroup(subgroup);

        // Initialize classGroups list
        List<ClassGroupDTO> classGroups = new ArrayList<>();

        // Get class group data
        Integer classGroupId = rs.getObject("id_class_group", Integer.class);
        if (classGroupId != null) {
            ClassGroupDTO classGroup = new ClassGroupDTO();
            classGroup.setId(classGroupId);
            classGroup.setDescription(rs.getString("class_group_description"));
            classGroup.setSubjectName(rs.getString("sub_name"));
            classGroup.setFormatName(rs.getString("frm_name"));
            classGroup.setTeacherName(rs.getString("teach_nname"));
            classGroup.setIdClassHold(rs.getInt("id_class_hold"));

            // Add only if unattested_count > 0
            if (rs.getInt("unattested_count") > 0) {
                classGroups.add(classGroup);
            }
        }

        student.setClassGroups(classGroups);
        return student;
    }
}
package com.example.backendtracker.domain.repositories.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentWithLoginMapper implements RowMapper<StudentWithLogin> {

    @Override
    public StudentWithLogin mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer idStudent = rs.getInt("id_student");
        String flpName = rs.getString("flp_name");
        String keyStudentParents = rs.getString("key_student_parents");
        String login = rs.getString("login");
        Integer idSubgroup = rs.getInt("id_subgroup");
        Integer idAcount = rs.getInt("id_account");

        return new StudentWithLogin(idStudent, flpName, keyStudentParents, login, idSubgroup, idAcount);
    }
}
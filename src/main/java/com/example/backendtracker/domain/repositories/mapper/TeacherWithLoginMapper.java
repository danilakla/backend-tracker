package com.example.backendtracker.domain.repositories.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherWithLoginMapper implements RowMapper<TeacherWithLogin> {

    @Override
    public TeacherWithLogin mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer idTeacher = rs.getInt("id_teacher");
        Integer idUniversity = rs.getInt("id_university");
        String flpName = rs.getString("flp_name");
        Integer idAccount = rs.getInt("id_account");
        String login = rs.getString("login");

        return new TeacherWithLogin(idTeacher, idUniversity, flpName, idAccount, login);
    }
}
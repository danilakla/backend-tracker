package com.example.backendtracker.domain.repositories.mapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeanWithLoginMapper implements RowMapper<DeanWithLogin> {

    @Override
    public DeanWithLogin mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer idDean = rs.getInt("id_dean");
        String flpName = rs.getString("flp_name");
        String faculty = rs.getString("faculty");
        Integer idUniversity = rs.getInt("id_university");
        Integer idAccount = rs.getInt("id_account");
        String login = rs.getString("login");

        return new DeanWithLogin(idDean, flpName, faculty, idUniversity, idAccount, login);
    }
}
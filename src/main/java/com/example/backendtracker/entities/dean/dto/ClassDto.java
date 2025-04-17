package com.example.backendtracker.entities.dean.dto;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassDto {
    private Integer idClass;
    private Boolean isAttestation;

    public ClassDto(Integer idClass, Boolean isAttestation) {
        this.idClass = idClass;
        this.isAttestation = isAttestation;
    }

    public Integer getIdClass() {
        return idClass;
    }

    public Boolean getIsAttestation() {
        return isAttestation;
    }

    public static class ClassDtoRowMapper implements RowMapper<ClassDto> {
        @Override
        public ClassDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ClassDto(
                    rs.getInt("idClass"),
                    rs.getBoolean("isAttestation")
            );
        }
    }
}
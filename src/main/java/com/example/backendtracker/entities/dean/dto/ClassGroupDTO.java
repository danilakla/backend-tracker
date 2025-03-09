package com.example.backendtracker.entities.dean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassGroupDTO {
    private Integer id;
    private String description;
    private String subjectName ;
    private String formatName;
    private String teacherName;
    private  Integer idClassHold;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassGroupDTO that = (ClassGroupDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    // Getters and Setters
}
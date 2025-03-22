package com.example.backendtracker.security.service.helper.student;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Objects;

@Data
class GroupAndSpecialtyKey {
    // Геттеры, equals и hashCode
    private String numberOfGroup;
    private int idSpecialty;
    private LocalDate date;

    public GroupAndSpecialtyKey(String numberOfGroup, int idSpecialty, LocalDate date) {
        this.numberOfGroup = numberOfGroup;
        this.idSpecialty = idSpecialty;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupAndSpecialtyKey that = (GroupAndSpecialtyKey) o;
        return idSpecialty == that.idSpecialty && Objects.equals(numberOfGroup, that.numberOfGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGroup, idSpecialty);
    }
}
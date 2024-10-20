package com.example.backendtracker.security.service.helper.student;

import lombok.Data;

import java.util.Objects;

@Data
class GroupAndSpecialtyKey {
    // Геттеры, equals и hashCode
    private String numberOfGroup;
    private int idSpecialty;

    public GroupAndSpecialtyKey(String numberOfGroup, int idSpecialty) {
        this.numberOfGroup = numberOfGroup;
        this.idSpecialty = idSpecialty;
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
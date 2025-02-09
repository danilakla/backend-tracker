package com.example.backendtracker.entities.common.dto;

import com.example.backendtracker.domain.models.Student;
import com.example.backendtracker.domain.models.Subgroup;
import com.example.backendtracker.domain.repositories.mapper.StudentWithLogin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SubGroupMember {

    private Subgroup subgroup;

    private List<StudentWithLogin> students;

}

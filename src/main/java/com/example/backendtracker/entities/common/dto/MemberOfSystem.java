package com.example.backendtracker.entities.common.dto;

import com.example.backendtracker.domain.models.Dean;
import com.example.backendtracker.domain.models.Teacher;
import com.example.backendtracker.domain.repositories.mapper.DeanWithLogin;
import com.example.backendtracker.domain.repositories.mapper.DeanWithLoginMapper;
import com.example.backendtracker.domain.repositories.mapper.TeacherWithLogin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberOfSystem {

    private List<DeanWithLogin> deanList;
    private List<TeacherWithLogin> teacherList;
}

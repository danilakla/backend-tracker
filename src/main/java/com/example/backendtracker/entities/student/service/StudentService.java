package com.example.backendtracker.entities.student.service;

import com.example.backendtracker.domain.repositories.ClassGroupRepository;
import com.example.backendtracker.domain.repositories.StudentRepository;
import com.example.backendtracker.domain.repositories.mapper.ClassGroupMapForStudentDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {
    private final ClassGroupRepository classGroupRepository;


    public List<ClassGroupMapForStudentDTO> getClassGroup(Integer subgroupId) {
        return classGroupRepository.findClassGroupsMapBySubgroupId(subgroupId);
    }
}

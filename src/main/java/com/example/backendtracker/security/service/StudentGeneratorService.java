package com.example.backendtracker.security.service;

import com.example.backendtracker.security.service.data.StudentExcelDto;
import com.example.backendtracker.security.service.helper.student.StudentInitializer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentGeneratorService {
    private final StudentInitializer studentInitializer;
    public void generateStudents(List<StudentExcelDto> studentExcelDtoList, Integer deanId) {

        studentInitializer.initStudent(studentExcelDtoList, deanId);

    }
}

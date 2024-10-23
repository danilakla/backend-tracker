package com.example.backendtracker.security.service;

import com.example.backendtracker.security.service.helper.student.dto.StudentExcelDto;
import com.example.backendtracker.security.service.helper.student.StudentInitializer;
import com.example.backendtracker.security.service.helper.student.dto.StudentResultDto;
import com.example.backendtracker.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentGeneratorService {
    private final StudentInitializer studentInitializer;
    private final EmailService emailService;

    public void generateStudents(List<StudentExcelDto> studentExcelDtoList, Integer deanId) throws MessagingException, IOException {

        List<StudentResultDto> studentResultDtos = studentInitializer.initStudent(studentExcelDtoList, deanId);
        //TODO REPLACE EMAIL TO DEAN EMAIL
        emailService.sendEmailWithAttachment("danilakartuzov5@gmail.com", studentResultDtos);
    }
}

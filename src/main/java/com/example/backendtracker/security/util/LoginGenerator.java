package com.example.backendtracker.security.util;

import com.example.backendtracker.security.service.helper.student.dto.StudentExcelDto;

import java.util.UUID;

public class LoginGenerator {

    public static String generateLogin(StudentExcelDto student) {
        return student.lastname() + "_" + student.name() + "_" + student.surname() + UUID.randomUUID().toString().substring(0, 8);
    }
}
package com.example.backendtracker.security.util;

import com.example.backendtracker.security.service.data.StudentExcelDto;

import java.util.UUID;

public class LoginGenerator {

    public static String generateLogin(StudentExcelDto student) {
        return student.surname() + "_" + student.name() + "_" + student.lastname() + UUID.randomUUID().toString().substring(0, 8);
    }
}
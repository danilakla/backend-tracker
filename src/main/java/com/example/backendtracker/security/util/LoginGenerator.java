package com.example.backendtracker.security.util;

import com.example.backendtracker.security.service.helper.student.dto.StudentExcelDto;
import com.example.backendtracker.util.NameConverter;

import java.util.UUID;

public class LoginGenerator {

    public static String generateLogin(StudentExcelDto student) {
        return NameConverter.convertNameToDb(student.lastname(), student.name(), student.surname()) + UUID.randomUUID().toString().substring(0, 8);
    }
}
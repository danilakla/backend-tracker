package com.example.backendtracker.util;

import com.example.backendtracker.domain.models.Student;

import java.util.Map;

public class FieldsUpdaterFromJson {
    public static void updateStudent(Student student, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            switch (key) {
                case "firstName":
                    if (value instanceof String) {
                        student.setFirstName((String) value);
                    }
                    break;
                case "lastName":
                    if (value instanceof String) {
                        student.setLastName((String) value);
                    }
                    break;
                case "patronymic":
                    if (value instanceof String) {
                        student.setPatronymic((String) value);
                    }
                    break;
                case "login":
                    if (value instanceof String) {
                        student.setLogin((String) value);
                    }
                    break;
                case "password":
                    if (value instanceof String) {
                        student.setPassword((String) value);
                    }
                    break;
                case "keyStudentParents":
                    if (value instanceof String) {
                        student.setKeyStudentParents((String) value);
                    }
                    break;
                case "idSubgroup":
                    if (value instanceof Integer) {
                        student.setIdSubgroup((Integer) value);
                    }
                    break;
                case "idAccount":
                    if (value instanceof Integer) {
                        student.setIdAccount((Integer) value);
                    }
                    break;
                default:
                    break;
            }
        });
    }
}

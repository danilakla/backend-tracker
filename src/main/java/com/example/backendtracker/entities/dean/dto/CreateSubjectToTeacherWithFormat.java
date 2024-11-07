package com.example.backendtracker.entities.dean.dto;

public record CreateSubjectToTeacherWithFormat(Integer teacherId, Integer subjectId, Integer formatClassId,
                                               String description) {
}

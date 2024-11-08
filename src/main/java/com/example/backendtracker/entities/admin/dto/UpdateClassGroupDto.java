package com.example.backendtracker.entities.admin.dto;

public record UpdateClassGroupDto(Integer teacherId, Integer classGroupId, Integer subjectId, Integer classFormatId,
                                  String description) {
}

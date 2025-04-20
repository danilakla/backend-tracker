package com.example.backendtracker.entities.admin.dto;

import java.util.List;

public record UpdateClassGroupDto(Integer teacherId, Integer classGroupId, Integer subjectId, Integer classFormatId,
                                  String description, Boolean hasApplyAttestation, List<Integer> idClassId) {
}

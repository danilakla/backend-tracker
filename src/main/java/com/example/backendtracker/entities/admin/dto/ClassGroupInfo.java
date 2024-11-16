package com.example.backendtracker.entities.admin.dto;

import com.example.backendtracker.domain.models.ClassGroup;
import lombok.Builder;

@Builder
public record ClassGroupInfo(String subjectName, String nameClassFormat, ClassGroup classGroup) {
}

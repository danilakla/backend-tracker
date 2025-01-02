package com.example.backendtracker.qrmanager.dto;

import lombok.Builder;
import lombok.Data;

@Builder
public record StudentReviewDto(int classId, int studentGradeId) {

}

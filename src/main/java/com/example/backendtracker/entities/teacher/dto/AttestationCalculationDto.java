package com.example.backendtracker.entities.teacher.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
public record AttestationCalculationDto(Integer maxLabCount, Integer holdId, Integer classId,Integer countClassThatNotAttestation, Double timeOfOneClass, List<Integer> studentId) {
}
package com.example.backendtracker.entities.teacher.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateClassInfo(Integer classGroupToSubgroupId, List<Integer> studentship) {
}

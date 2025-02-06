package com.example.backendtracker.entities.admin.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AssignGroupsToClass(Boolean isMany, Integer classGroupId, Boolean hasApplyAttestation, List<Integer> studentGroupIds) {
}

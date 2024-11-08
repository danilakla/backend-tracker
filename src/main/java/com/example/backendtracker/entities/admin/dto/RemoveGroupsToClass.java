package com.example.backendtracker.entities.admin.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RemoveGroupsToClass(Integer classGroupId, List<Integer> studentGroupIds) {
}

package com.example.backendtracker.entities.admin.dto;

import com.example.backendtracker.domain.models.ClassGroup;
import lombok.Builder;

import java.util.List;
@Builder
public record ClassGroupDto(ClassGroup classGroup, List<Integer> subgroupsId) {
}

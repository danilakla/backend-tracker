package com.example.backendtracker.entities.admin.dto;

import com.example.backendtracker.domain.models.ClassGroup;
import com.example.backendtracker.domain.models.ClassGroupsToSubgroups;
import lombok.Builder;

import java.util.List;

@Builder
public record ClassGroupDto(ClassGroupInfo classGroup, List<ClassGroupsToSubgroups> subgroupsId) {
}

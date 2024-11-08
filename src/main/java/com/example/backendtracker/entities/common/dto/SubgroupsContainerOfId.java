package com.example.backendtracker.entities.common.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SubgroupsContainerOfId(List<Integer> ids) {
}

package com.example.backendtracker.entities.teacher.dto;

import com.example.backendtracker.domain.models.Subgroup;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ClassGroupClassHoldDTO {
    private List<ClassHoldSubgroupDTO> classHoldSubgroupDTO;
    private Boolean isOneClass;
}
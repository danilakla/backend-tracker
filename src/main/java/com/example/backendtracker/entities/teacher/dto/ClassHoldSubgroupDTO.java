package com.example.backendtracker.entities.teacher.dto;

import com.example.backendtracker.domain.models.Subgroup;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClassHoldSubgroupDTO {
    private Integer idHold;
    private Subgroup subgroup;
}
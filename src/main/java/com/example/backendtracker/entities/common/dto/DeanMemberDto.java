package com.example.backendtracker.entities.common.dto;

import com.example.backendtracker.domain.models.Dean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class DeanMemberDto {
    private Dean dean;
    private List<SubGroupMember> subGroupMemberList;

}

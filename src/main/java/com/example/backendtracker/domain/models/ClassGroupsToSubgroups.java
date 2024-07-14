package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("ClassGroupsToSubgroups")
public class ClassGroupsToSubgroups {
    @Id
    private Integer idSubgroup;
    @Id
    private Integer idClassGroup;
}

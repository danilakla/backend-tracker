package com.example.backendtracker.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
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

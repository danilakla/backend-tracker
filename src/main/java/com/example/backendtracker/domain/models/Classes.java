package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("classes")
public class Classes {
    @Id
    private Integer idClass;
    private Integer idClassGroupToSubgroup;
}

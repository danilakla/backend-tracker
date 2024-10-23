package com.example.backendtracker.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("admins")
public class Admin {
    @Id
    private Integer idAdmin;
    private String flpName;
    private Integer idAccount;
}
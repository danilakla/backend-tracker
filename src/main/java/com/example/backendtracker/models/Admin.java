package com.example.backendtracker.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("Admins")
public class Admin {
    @Id
    private Integer idAdmin;
    private String firstName;
    private String lastName;
}

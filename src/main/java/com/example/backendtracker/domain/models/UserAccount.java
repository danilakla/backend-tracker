package com.example.backendtracker.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("UserAccounts")
public class UserAccount {
    @Id
    private Integer idAccount;
    private String login;
    private String password;
    private Integer idRole;

}

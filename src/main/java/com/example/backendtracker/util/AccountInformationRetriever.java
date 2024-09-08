package com.example.backendtracker.util;

import com.example.backendtracker.domain.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AccountInformationRetriever {
    private final DeanRepository deanRepository;
    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final UserAccountRepository userAccountRepository;

    public Integer getAccountId(UserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().toString();
        Integer AccountId = null;
        Integer userAccount = userAccountRepository.findByLogin(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Can't retrieve user account by the account id")).getIdAccount();
        if (Objects.equals(role, "ROLE_ADMIN")) {
            AccountId = adminRepository.findByIdAccount(userAccount).orElseThrow(() -> new RuntimeException("Can't retrieve the account id")).getIdAdmin();
        } else if (Objects.equals(role, "ROLE_DEAN")) {
            AccountId = deanRepository.findByIdAccount(userAccount).orElseThrow(() -> new RuntimeException("Can't retrieve the account id")).getIdDean();

        } else if (Objects.equals(role, "ROLE_TEACHER")) {
            AccountId = teacherRepository.findByIdAccount(userAccount).orElseThrow(() -> new RuntimeException("Can't retrieve the account id")).getIdTeacher();

        } else if (Objects.equals(role, "ROLE_STUDENT")) {
            AccountId = studentRepository.findByIdAccount(userAccount).orElseThrow(() -> new RuntimeException("Can't retrieve the account id")).getIdStudent();

        }

        return AccountId;
    }
}

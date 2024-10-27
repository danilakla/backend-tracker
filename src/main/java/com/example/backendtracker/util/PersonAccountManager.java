package com.example.backendtracker.util;

import com.example.backendtracker.domain.models.*;
import com.example.backendtracker.domain.repositories.*;
import com.example.backendtracker.security.controller.dto.UserInfoDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class PersonAccountManager {

    private final DeanRepository deanRepository;
    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final UserAccountRepository userAccountRepository;


    public void updateUserInfo(UserDetails userDetails, UserInfo userInfo) {

        String role = userDetails.getAuthorities().iterator().next().toString();

        UserAccount userAccount = userAccountRepository.findByLogin(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Can't retrieve user account by the account id"));
        Integer userAccountId = userAccount.getIdAccount();
        if (Objects.equals(role, "ROLE_ADMIN")) {
            Admin adminAccount = adminRepository.findByIdAccount(userAccountId).orElseThrow(() -> new RuntimeException("Can't retrieve the account id"));
            adminAccount.setFlpName(NameConverter.convertNameToDb(userInfo.lastname(), userInfo.name(), userInfo.surname()));
            adminRepository.save(adminAccount);
        } else if (Objects.equals(role, "ROLE_DEAN")) {
            Dean deanAccount = deanRepository.findByIdAccount(userAccountId).orElseThrow(() -> new RuntimeException("Can't retrieve the account id"));
            deanAccount.setFlpName(NameConverter.convertNameToDb(userInfo.lastname(), userInfo.name(), userInfo.surname()));
            deanRepository.save(deanAccount);
        } else if (Objects.equals(role, "ROLE_TEACHER")) {
            Teacher teacherAccount = teacherRepository.findByIdAccount(userAccountId).orElseThrow(() -> new RuntimeException("Can't retrieve the account id"));
            teacherAccount.setFlpName(NameConverter.convertNameToDb(userInfo.lastname(), userInfo.name(), userInfo.surname()));
            teacherRepository.save(teacherAccount);
        } else if (Objects.equals(role, "ROLE_STUDENT")) {
            Student studentAccount = studentRepository.findByIdAccount(userAccountId).orElseThrow(() -> new RuntimeException("Can't retrieve the account id"));
            studentAccount.setFlpName(NameConverter.convertNameToDb(userInfo.lastname(), userInfo.name(), userInfo.surname()));

            studentRepository.save(studentAccount);
        }

    }


}

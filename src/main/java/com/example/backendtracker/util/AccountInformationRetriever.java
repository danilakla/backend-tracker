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

    public Integer getUniversityId(UserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().toString();
        Integer univerId = null;
        Integer userAccount = userAccountRepository.findByLogin(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Can't retrieve user account by the account id")).getIdAccount();
        if (Objects.equals(role, "ROLE_DEAN")) {
            univerId = deanRepository.findByIdAccount(userAccount).orElseThrow(() -> new RuntimeException("Can't retrieve the account id")).getIdUniversity();

        } else if (Objects.equals(role, "ROLE_TEACHER")) {
            univerId = teacherRepository.findByIdAccount(userAccount).orElseThrow(() -> new RuntimeException("Can't retrieve the account id")).getIdUniversity();

        }
        return univerId;

    }

    public UserInfoDto getAccountInfo(UserDetails userDetails) {

        String role = userDetails.getAuthorities().iterator().next().toString();
        UserInfoDto userInfoDto = UserInfoDto.builder().role(role).build();

        UserAccount userAccount = userAccountRepository.findByLogin(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Can't retrieve user account by the account id"));
        Integer userAccountId = userAccount.getIdAccount();
        userInfoDto.setLogin(userAccount.getLogin());
        if (Objects.equals(role, "ROLE_ADMIN")) {
            Admin adminAccount = adminRepository.findByIdAccount(userAccountId).orElseThrow(() -> new RuntimeException("Can't retrieve the account id"));
            setUpUserInfo(adminAccount.getFlpName(), userInfoDto);
        } else if (Objects.equals(role, "ROLE_DEAN")) {
            Dean deanAccount = deanRepository.findByIdAccount(userAccountId).orElseThrow(() -> new RuntimeException("Can't retrieve the account id"));
            setUpUserInfo(deanAccount.getFlpName(), userInfoDto);

        } else if (Objects.equals(role, "ROLE_TEACHER")) {
            Teacher teacherAccount = teacherRepository.findByIdAccount(userAccountId).orElseThrow(() -> new RuntimeException("Can't retrieve the account id"));
            setUpUserInfo(teacherAccount.getFlpName(), userInfoDto);

        } else if (Objects.equals(role, "ROLE_STUDENT")) {
            Student studentAccount = studentRepository.findByIdAccount(userAccountId).orElseThrow(() -> new RuntimeException("Can't retrieve the account id"));
            setUpUserInfo(studentAccount.getFlpName(), userInfoDto);

        }

        return userInfoDto;
    }

    private UserInfoDto setUpUserInfo(String flpName, UserInfoDto userInfoDto) {
        UserInfo userInfo = NameConverter.convertNameToJavaClass(flpName);
        userInfoDto.setName(userInfo.name());
        userInfoDto.setSurname(userInfo.surname());
        userInfoDto.setLastname(userInfo.lastname());
        return userInfoDto;
    }
}

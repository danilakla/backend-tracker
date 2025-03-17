package com.example.backendtracker.security.service;

import com.example.backendtracker.domain.models.Student;
import com.example.backendtracker.domain.models.UserAccount;
import com.example.backendtracker.domain.repositories.StudentRepository;
import com.example.backendtracker.domain.repositories.UserAccountRepository;
import com.example.backendtracker.reliability.exception.BadRequestException;
import com.example.backendtracker.security.controller.dto.ParentToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ParentService {

    private StudentRepository studentRepository;
    private UserAccountRepository userAccountRepository;


    public String getLoginForParentToken(ParentToken parentToken) {
        Student student = studentRepository.findStudentByKeyStudentParents(parentToken.token()).orElseThrow(() -> new BadRequestException("Некорректный ключ родителя,"));
        UserAccount userAccount = userAccountRepository.findById(student.getIdAccount()).orElseThrow(() -> new BadRequestException("Некорректный ключ родителя"));
        return userAccount.getLogin();
    }
}

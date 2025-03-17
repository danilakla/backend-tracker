package com.example.backendtracker.security.controller;

import com.example.backendtracker.security.service.StudentGeneratorService;
import com.example.backendtracker.security.service.helper.student.dto.StudentExcelDto;
import com.example.backendtracker.util.AccountInformationRetriever;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("dean")
public class StudentGeneratorController {
    private final StudentGeneratorService studentGeneratorService;
    private final AccountInformationRetriever accountInformationRetriever;

    @PostMapping("generate-student")
    public ResponseEntity<?> registerUser(@RequestBody List<StudentExcelDto> userRegistrationRequest, @AuthenticationPrincipal UserDetails userDetails) throws MessagingException, IOException {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return  studentGeneratorService.generateStudents(userRegistrationRequest, userDetails.getUsername(), accountId);
    }
}

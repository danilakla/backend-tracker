package com.example.backendtracker.security.controller;

import com.example.backendtracker.security.service.StudentGeneratorService;
import com.example.backendtracker.security.service.UserAccountService;
import com.example.backendtracker.security.service.data.StudentExcelDto;
import com.example.backendtracker.util.AccountInformationRetriever;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("dean")
public class StudentGeneratorController {
    private final StudentGeneratorService studentGeneratorService;
    private final AccountInformationRetriever accountInformationRetriever;

    @PostMapping("generate-student")
    public ResponseEntity<?> registerUser(@RequestBody List<StudentExcelDto> userRegistrationRequest, @AuthenticationPrincipal UserDetails userDetails) {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        studentGeneratorService.generateStudents(userRegistrationRequest, accountId);
        return ResponseEntity.status(201).build();
    }
}

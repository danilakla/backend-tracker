package com.example.backendtracker.entities.admin.controller;

import com.example.backendtracker.entities.admin.dto.UniversityCreateDto;
import com.example.backendtracker.entities.admin.service.AdminService;
import com.example.backendtracker.security.exception.InvalidEncryptionProcessException;
import com.example.backendtracker.util.AccountInformationRetriever;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("admin")
public class AdminController {

    private final AccountInformationRetriever accountInformationRetriever;
    private final AdminService adminService;

    @GetMapping("key/university_id")
    public String generateKeyWithUniversityId(@AuthenticationPrincipal UserDetails userDetails) throws InvalidEncryptionProcessException {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return adminService.generateKeyBasedOnUniversityId(accountId);
    }

    @PostMapping("university/create")
    public String createUniversity(@RequestBody UniversityCreateDto universityCreateDto, @AuthenticationPrincipal UserDetails userDetails) {
        adminService.createUniversity(universityCreateDto, accountInformationRetriever.getAccountId(userDetails));
        return "ok";
    }
}

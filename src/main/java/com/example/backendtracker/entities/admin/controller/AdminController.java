package com.example.backendtracker.entities.admin.controller;

import com.example.backendtracker.entities.admin.dto.DeanInfoForKeyDto;
import com.example.backendtracker.entities.admin.dto.TeacherInfoForKeyDto;
import com.example.backendtracker.entities.admin.dto.UniversityCreateDto;
import com.example.backendtracker.entities.admin.service.AdminService;
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

    @GetMapping("dean/key")
    public String generateKeyWithUniversityIdForDean(@RequestBody DeanInfoForKeyDto deanInfoForKeyDto, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return adminService.generateKey(accountId, deanInfoForKeyDto.roleName(), deanInfoForKeyDto.faculty());
    }

    @GetMapping("teacher/key")
    public String generateKeyWithUniversityIdForTeacher(@RequestBody TeacherInfoForKeyDto teacherInfoForKeyDto, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return adminService.generateKey(accountId, teacherInfoForKeyDto.roleName());
    }


    @PostMapping("university/create")
    public String createUniversity(@RequestBody UniversityCreateDto universityCreateDto, @AuthenticationPrincipal UserDetails userDetails) {
        adminService.createUniversity(universityCreateDto, accountInformationRetriever.getAccountId(userDetails));
        return "ok";
    }
}

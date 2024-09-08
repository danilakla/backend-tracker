package com.example.backendtracker.entities.admin.controller;

import com.example.backendtracker.entities.admin.dto.RoleDto;
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

    @GetMapping("dean/key/university_id")
    public String generateKeyWithUniversityIdForDean(@RequestBody RoleDto roleDto, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return adminService.generateKeyBasedOnUniversityId(accountId,roleDto.roleName());
    }

    @GetMapping("teacher/key/university_id")
    public String generateKeyWithUniversityIdForTeacher(@RequestBody RoleDto roleDto,  @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return adminService.generateKeyBasedOnUniversityId(accountId,roleDto.roleName());
    }



    @PostMapping("university/create")
    public String createUniversity(@RequestBody UniversityCreateDto universityCreateDto, @AuthenticationPrincipal UserDetails userDetails) {
        adminService.createUniversity(universityCreateDto, accountInformationRetriever.getAccountId(userDetails));
        return "ok";
    }
}

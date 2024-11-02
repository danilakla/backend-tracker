package com.example.backendtracker.entities.admin.controller;

import com.example.backendtracker.domain.models.Teacher;
import com.example.backendtracker.domain.models.University;
import com.example.backendtracker.entities.admin.dto.*;
import com.example.backendtracker.entities.admin.service.AdminService;
import com.example.backendtracker.entities.common.CommonService;
import com.example.backendtracker.entities.common.dto.MemberOfSystem;
import com.example.backendtracker.util.AccountInformationRetriever;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("admin")
public class AdminController {

    private final AccountInformationRetriever accountInformationRetriever;
    private final AdminService adminService;

    @PostMapping("dean/key")
    public String generateKeyWithUniversityIdForDean(@RequestBody DeanInfoForKeyDto deanInfoForKeyDto, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return adminService.generateKey(accountId, deanInfoForKeyDto.roleName(), deanInfoForKeyDto.faculty());
    }

    @PostMapping("teacher/key")
    public String generateKeyWithUniversityIdForTeacher(@RequestBody TeacherInfoForKeyDto teacherInfoForKeyDto, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return adminService.generateKey(accountId, teacherInfoForKeyDto.roleName());
    }


    @PostMapping("university/create")
    public String createUniversity(@RequestBody UniversityCreateDto universityCreateDto, @AuthenticationPrincipal UserDetails userDetails) {
        adminService.createUniversity(universityCreateDto, accountInformationRetriever.getAccountId(userDetails));
        return "ok";
    }

    @PostMapping("university/update")
    public ResponseEntity<ResponseUniversityDto> updateUniversity(@RequestBody UniversityUpdateDto universityUpdateDto) {
        University university = adminService.updateUniversity(universityUpdateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUniversityDto
                .builder()
                .idUniversity(university.getIdUniversity())
                .name(university.getName())
                .description(university.getDescription())
                .build());
    }

    @GetMapping("university/get")
    public ResponseEntity<ResponseUniversityDto> getUniversity(@AuthenticationPrincipal UserDetails userDetails) {

        University university = adminService.getUniversity(accountInformationRetriever.getAccountId(userDetails));
        return ResponseEntity.status(HttpStatus.OK).body(ResponseUniversityDto
                .builder()
                .idUniversity(university.getIdUniversity())
                .name(university.getName())
                .description(university.getDescription())
                .build());
    }

    @GetMapping("members/get")
    public ResponseEntity<MemberOfSystem> getMemberOfSystem(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getMembers(accountInformationRetriever.getAccountId(userDetails)));
    }

    @DeleteMapping("delete/teacher")
    public ResponseEntity<TypeResolutionContext.Empty> deleteTeacher(@RequestBody TeacherDeleteDto teacherDeleteDto) {
        adminService.deleteTeacher(teacherDeleteDto.teacherId(), teacherDeleteDto.newTeacherId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("delete/dean")
    public ResponseEntity<TypeResolutionContext.Empty> deleteDean(@RequestBody DeanDeleteDto deanDeleteDto) {
        adminService.deleteDean(deanDeleteDto.deanId(), deanDeleteDto.newDeanId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

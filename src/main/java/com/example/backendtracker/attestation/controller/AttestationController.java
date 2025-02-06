package com.example.backendtracker.attestation.controller;

import com.example.backendtracker.attestation.service.AttestationService;
import com.example.backendtracker.domain.models.ClassGroup;
import com.example.backendtracker.domain.models.Teacher;
import com.example.backendtracker.entities.dean.dto.CreateSubjectToTeacherWithFormat;
import com.example.backendtracker.util.AccountInformationRetriever;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("attestation")
@AllArgsConstructor
public class AttestationController {

    private final AttestationService attestationService;
    private final AccountInformationRetriever accountInformationRetriever;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public void createAttestation(@RequestBody ExpirationTimeInSeconds expirationTimeInSeconds, @AuthenticationPrincipal UserDetails userDetails) {
        Integer deanId = accountInformationRetriever.getAccountId(userDetails);
        attestationService.createStudentAttestationGrade(deanId,expirationTimeInSeconds.time());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("notify-dean")
    public List<Teacher> notifyDean(@AuthenticationPrincipal UserDetails userDetails) {
        Integer deanId = accountInformationRetriever.getAccountId(userDetails);
        return attestationService.notificationDean(deanId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("notify-teacher/{holdId}")
    public Boolean notifyTeacher(@PathVariable(name = "holdId") Integer holdId, @AuthenticationPrincipal UserDetails userDetails) {
        Integer teacherId = accountInformationRetriever.getAccountId(userDetails);
        return attestationService.isHoldIdInTeacher(teacherId, holdId);
    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("remove/{holdId}")
    public void removeHoldIdFromTeacher(@PathVariable(name = "holdId") Integer holdId, @AuthenticationPrincipal UserDetails userDetails) {
        Integer teacherid = accountInformationRetriever.getAccountId(userDetails);
        attestationService.removeHoldIdFromTeacher(teacherid, holdId);
    }

}

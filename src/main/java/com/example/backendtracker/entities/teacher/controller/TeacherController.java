package com.example.backendtracker.entities.teacher.controller;

import com.example.backendtracker.domain.models.AttestationStudentGrade;
import com.example.backendtracker.domain.models.ClassGroup;
import com.example.backendtracker.domain.models.Classes;
import com.example.backendtracker.domain.models.StudentGrade;
import com.example.backendtracker.domain.repositories.mapper.ClassGroupMapDTO;
import com.example.backendtracker.entities.admin.dto.ClassGroupDto;
import com.example.backendtracker.entities.dean.service.DeanService;
import com.example.backendtracker.entities.teacher.dto.*;
import com.example.backendtracker.entities.teacher.service.TeacherService;
import com.example.backendtracker.util.AccountInformationRetriever;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    private final TeacherService teacherService;
    private final AccountInformationRetriever accountInformationRetriever;

    @GetMapping("get/class-groups")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ClassGroupMapDTO>> getListClassGroup(@AuthenticationPrincipal UserDetails userDetails) {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return ResponseEntity.ok(teacherService.getListClassGroups(accountId));
    }

    @DeleteMapping("delete/class/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteClass(@PathVariable("id") Integer id) {

        teacherService.deleteClass(id);
        return ResponseEntity.noContent().build();

    }


    @GetMapping("get/class-groups/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ClassGroupDto> getListClassGroup(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return ResponseEntity.ok(teacherService.getClassGroup(id, accountId));
    }

    @PutMapping("update/classes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StudentGrade> updateClasses(@RequestBody UpdateStudentGrade updateStudentGrade) {
        return ResponseEntity.ok(teacherService.updateStudentGrade(updateStudentGrade));
    }

    @PutMapping("update/attestation/classes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AttestationStudentGrade> updateAttestationClasses(@RequestBody UpdateAttestationStudentGrade updateAttestationStudentGrade) {
        return ResponseEntity.ok(teacherService.updateAttestationStudentGrade(updateAttestationStudentGrade));
    }

    @PostMapping("calculate/avg/attestation")
    @ResponseStatus(HttpStatus.OK)
    public List<AttestationStudentGrade> calculateAvgAttestation(@RequestBody AttestationCalculationDto attestationCalculationDto) {
        return teacherService.calculateAvgForAttestationProp(attestationCalculationDto);
    }


    @PostMapping("accept/attendance")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> acceptAttendance(@RequestBody StudentAttendanceDto studentAttendanceDto) throws BadPaddingException {
        teacherService.acceptAttendance(studentAttendanceDto.studentGrateId(), studentAttendanceDto.attendanceCode());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("create/classes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ClassInfoDto> createClasses(@RequestBody CreateClassInfo createClassInfo) {
        return ResponseEntity.ok(teacherService.generateStudentGrateForNewClass(createClassInfo));
    }

}

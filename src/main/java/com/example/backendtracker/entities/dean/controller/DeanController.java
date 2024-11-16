package com.example.backendtracker.entities.dean.controller;

import com.example.backendtracker.domain.mapper.UniversalMapper;
import com.example.backendtracker.domain.models.*;
import com.example.backendtracker.domain.repositories.mapper.ClassGroupMapDTO;
import com.example.backendtracker.entities.admin.dto.AssignGroupsToClass;
import com.example.backendtracker.entities.admin.dto.ClassGroupDto;
import com.example.backendtracker.entities.admin.dto.RemoveGroupsToClass;
import com.example.backendtracker.entities.admin.dto.UpdateClassGroupDto;
import com.example.backendtracker.entities.common.dto.MemberOfSystem;
import com.example.backendtracker.entities.common.dto.SubGroupMember;
import com.example.backendtracker.entities.dean.dto.*;
import com.example.backendtracker.entities.dean.service.DeanService;
import com.example.backendtracker.util.AccountInformationRetriever;
import com.example.backendtracker.util.UserInfo;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("dean")
public class DeanController {

    private final AccountInformationRetriever accountInformationRetriever;
    private final DeanService deanService;

    @DeleteMapping("class-group/delete/{id}")
    public ResponseEntity<ClassGroup> deleteClassGroup(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return ResponseEntity.ok(deanService.deleteClassGroup(id, accountId));
    }

    @PutMapping("class-group/update")
    public ResponseEntity<ClassGroup> updateClassGroup(@RequestBody UpdateClassGroupDto updateClassGroupDto, @AuthenticationPrincipal UserDetails userDetails) {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(deanService.updateClassGroup(updateClassGroupDto, accountId));
    }


    @PostMapping("class-group/create")
    public ResponseEntity<ClassGroup> createClassGroup(@RequestBody CreateSubjectToTeacherWithFormat createSubjectToTeacherWithFormat, @AuthenticationPrincipal UserDetails userDetails) {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        Integer universityId = accountInformationRetriever.getUniversityId(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(deanService.createClassGroup(createSubjectToTeacherWithFormat, accountId, universityId));
    }

    @PostMapping("assign/groups")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createClassGroup(@RequestBody AssignGroupsToClass assignGroupsToClass) {
        deanService.assignGroupsToClass(assignGroupsToClass);
    }

    @PostMapping("add/groups-to-class")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addClassGroup(@RequestBody AssignGroupsToClass assignGroupsToClass) {
        deanService.addSubGroupsToClassGroup(assignGroupsToClass);
    }

    @PostMapping("remove/groups-from-class")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeClassGroup(@RequestBody RemoveGroupsToClass removeGroupsToClass) {
        deanService.removeSubGroupsToClassGroup(removeGroupsToClass);
    }

    @GetMapping("get/class-groups")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ClassGroupMapDTO>> getListClassGroup(@AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return ResponseEntity.ok(deanService.getListClassGroup(accountId));
    }

    @GetMapping("get/class-group/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ClassGroupDto> getClassGroup(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return ResponseEntity.ok(deanService.getClassGroup(id, accountId));
    }

    @GetMapping("get/class-groups/subject/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ClassGroup>> getListClassGroupBySubjectId(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return ResponseEntity.ok(deanService.getListClassGroupByIdSubject(id, accountId));
    }

    @PostMapping("specialty/create")
    public ResponseEntity<Specialty> createSpecialty(@RequestBody CreateSpecialtyDto specialtyDto, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);


        Specialty specialty = deanService.createSpecialty(specialtyDto, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(specialty);
    }

    @GetMapping("subject/get/all")
    public ResponseEntity<List<Subject>> getAllSubject(@AuthenticationPrincipal UserDetails userDetails) {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(deanService.getListSubjects(accountId));
    }


    @GetMapping("class-format/get/all")
    public ResponseEntity<List<ClassFormat>> getAllClassFormat(@AuthenticationPrincipal UserDetails userDetails) {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(deanService.getListClassFormat(accountId));
    }


    @GetMapping("specialty/get/all")
    public ResponseEntity<List<Specialty>> getAllSpecialty(@AuthenticationPrincipal UserDetails userDetails) {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(deanService.getSetSpecialty(accountId));
    }

    @GetMapping("specialty/get/{id}")
    public ResponseEntity<Specialty> getSpecialty(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);


        Specialty specialty = deanService.getSpecialty(id, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(specialty);
    }

    @DeleteMapping("specialty/delete/{id}")
    public ResponseEntity<Specialty> deleteSpecialty(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);


        Specialty specialty = deanService.deleteSpecialty(id, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(specialty);
    }


    @PutMapping("specialty/put")
    public ResponseEntity<Specialty> putSpecialty(@RequestBody UpdateSpecialty updateSpecialty, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);


        Specialty specialty = deanService.updateSpecialty(updateSpecialty, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(specialty);
    }


    @PostMapping("class-format/create")
    public ResponseEntity<ClassFormat> createClassFormat(@RequestBody CreateClassFormatRequestDTO createClassFormatRequestDTO, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);


        ClassFormat classFormat = deanService.createClassFormat(createClassFormatRequestDTO, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(classFormat);
    }

    @GetMapping("class-format/get/{id}")
    public ResponseEntity<ClassFormat> getClassFormat(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);


        ClassFormat classFormat = deanService.getClassFormat(id, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(classFormat);
    }

    @DeleteMapping("class-format/delete/{id}")
    public ResponseEntity<ClassFormat> deleteClassFormat(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);


        ClassFormat classFormat = deanService.deleteClassFormat(id, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(classFormat);
    }


    @PutMapping("class-format/put")
    public ResponseEntity<ClassFormat> putClassFormat(@RequestBody UpdateClassFormatRequestDTO updateClassFormatRequestDTO, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);


        ClassFormat classFormat = deanService.updateClassFormat(updateClassFormatRequestDTO, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(classFormat);
    }

    @PostMapping("subject/create")
    public ResponseEntity<Subject> createSubject(@RequestBody CreateSubjectDto createSubjectDto, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);


        Subject subject = deanService.createSubject(createSubjectDto, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body(subject);
    }

    @GetMapping("subject/get/{id}")
    public ResponseEntity<Subject> getSubject(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);


        Subject subject = deanService.getSubject(id, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(subject);
    }

    @DeleteMapping("subject/delete/{id}")
    public ResponseEntity<Subject> deleteSubject(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);


        Subject subject = deanService.deleteSubject(id, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(subject);
    }


    @PutMapping("subject/put")
    public ResponseEntity<Subject> putSubject(@RequestBody UpdateSubjectDto updateSubjectDto, @AuthenticationPrincipal UserDetails userDetails) {

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);


        Subject subject = deanService.updateSubject(updateSubjectDto, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(subject);
    }

    @GetMapping("members/get")
    public ResponseEntity<List<SubGroupMember>> getMemberOfSystem(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(deanService.getSubGroupMembers(accountInformationRetriever.getAccountId(userDetails)));
    }

    @DeleteMapping("delete/subgroup/{id}")
    public ResponseEntity<Subgroup> deleteSubgroupById(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        Integer accountId = accountInformationRetriever.getAccountId(userDetails);
        return ResponseEntity.ok(deanService.deleteByIdSubgroup(id, accountId));
    }

    @GetMapping("subgroup/get")
    public ResponseEntity<List<Subgroup>> getSubgroup(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(deanService.getListSubgroups(accountInformationRetriever.getAccountId(userDetails)));
    }


    @PostMapping("students/create")
    public ResponseEntity<Student> createStudentAccounts(@RequestBody StudentAddDto studentAddDto, @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.status(HttpStatus.CREATED).body(deanService.addStudent(studentAddDto));
    }

    @PostMapping("students/update/{id}")
    public ResponseEntity<Student> updateStudentAccounts(@PathVariable("id") Integer id, @RequestBody UserInfo userInfo, @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.status(HttpStatus.CREATED).body(deanService.updateStudentInfo(userInfo, id));
    }


}

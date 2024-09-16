package com.example.backendtracker.entities.dean.controller;
import com.example.backendtracker.domain.mapper.UniversalMapper;
import com.example.backendtracker.entities.dean.dto.CreateClassFormatRequestDTO;
import com.example.backendtracker.entities.dean.dto.CreateSpecialtyDto;
import com.example.backendtracker.entities.dean.service.DeanService;
import com.example.backendtracker.util.AccountInformationRetriever;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("dean")
public class DeanController {

    private final AccountInformationRetriever accountInformationRetriever;
    private final DeanService deanService;


    @PostMapping("specialty/create")
    public String createSpecialty(@RequestBody CreateSpecialtyDto specialtyDto,  @AuthenticationPrincipal UserDetails userDetails){

        Integer accountId = accountInformationRetriever.getAccountId(userDetails);


        UniversalMapper universalMapper = new UniversalMapper();

        deanService.createSpecialty(specialtyDto,accountId);
        return "create specialty successful";
    }


    @PostMapping("students/create")
    public String createStudentAccounts(@AuthenticationPrincipal UserDetails userDetails){

        return "";
    }
    @PostMapping("/class-format/create")
    @ResponseStatus(HttpStatus.OK)
    public String createClassFormat(@RequestBody CreateClassFormatRequestDTO createClassFormatRequestDTO) {
        return "ok";
    }
    @PostMapping("/class-format/update")
    public String updateClassFormat() {
        return "ok";
    }
    @GetMapping("/class-formats")
    public String getClassFormats() {
        return "ok";
    }
    @GetMapping("/teachers")
    public String getTeachers() {
        return "ok";
    }
    @GetMapping("/groups")
    public String getGroups() {
        return "ok";
    }
    @PostMapping("/group/add-student")
    public String addStudentToGroup() {
        return "ok";
    }
    @DeleteMapping("/group/delete-student")
    public String deleteStudentFromGroup() {
        return "ok";
    }
    @PostMapping("/subject/create")
    public String createSubject() {
        return "ok";
    }
    @PutMapping("/subject/update")
    public String updateSubject() {
        return "ok";
    }
    @DeleteMapping("/subject/delete")
    public String deleteSubject() {
        return "ok";
    }
    @GetMapping("/subjects")
    public String getSubjects() {
        return "ok";
    }
}

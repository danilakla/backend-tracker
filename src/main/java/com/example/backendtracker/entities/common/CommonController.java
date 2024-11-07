package com.example.backendtracker.entities.common;

import com.example.backendtracker.domain.models.Student;
import com.example.backendtracker.domain.models.Teacher;
import com.example.backendtracker.util.AccountInformationRetriever;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("common")
@AllArgsConstructor
public class CommonController {


    private final CommonService commonService;

    private final AccountInformationRetriever accountInformationRetriever;

    @GetMapping("teachers")
    public ResponseEntity<List<Teacher>> listStudents(@AuthenticationPrincipal UserDetails userDetails) {
        Integer universityId = accountInformationRetriever.getUniversityId(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(commonService.getListTeachers(universityId));
    }
}

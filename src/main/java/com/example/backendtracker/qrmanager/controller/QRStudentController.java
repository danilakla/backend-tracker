package com.example.backendtracker.qrmanager.controller;

import com.example.backendtracker.qrmanager.dto.ReviewDto;
import com.example.backendtracker.qrmanager.dto.StudentReviewDto;
import com.example.backendtracker.qrmanager.entity.ClassGeneral;
import com.example.backendtracker.qrmanager.service.QRService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("qr/student")
public class QRStudentController {

    @Autowired
    private QRService userService;


    @GetMapping("key")
    public ResponseEntity<ClassGeneral> getKey(@RequestParam Integer id) throws BadRequestException {
        ClassGeneral user = userService.getById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
        return new ResponseEntity<>(user, HttpStatusCode.valueOf(200));
    }

    @PostMapping("ask/review")
    public ResponseEntity<Void> askReview(@RequestBody StudentReviewDto studentReviewDto) throws BadRequestException {
        userService.reviewStudent(studentReviewDto.classId(), studentReviewDto.studentGradeId());
        return ResponseEntity.noContent().build();
    }

}
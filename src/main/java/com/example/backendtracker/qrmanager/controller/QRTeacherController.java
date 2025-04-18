package com.example.backendtracker.qrmanager.controller;

import com.example.backendtracker.domain.models.StudentGrade;
import com.example.backendtracker.domain.repositories.StudentGradeRepository;
import com.example.backendtracker.qrmanager.dto.ReviewDto;
import com.example.backendtracker.qrmanager.entity.ClassGeneral;
import com.example.backendtracker.qrmanager.entity.StudentReview;
import com.example.backendtracker.qrmanager.service.QRService;
import jakarta.websocket.server.PathParam;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("qr/teacher")
public class QRTeacherController {
@Autowired
private StudentGradeRepository studentGradeRepository;
    @Autowired
    private QRService userService;

    @PostMapping("save/key")
    public ResponseEntity<ClassGeneral> saveKey(@RequestBody ClassGeneral user) {
        return new ResponseEntity<>(userService.save(user), HttpStatusCode.valueOf(201));
    }

    @PostMapping("start")
    public ResponseEntity<Void> startQrProcess(@RequestBody ReviewDto reviewDto) {
        userService.delete(reviewDto.classId());
        userService.initProcessOfQrManagement(reviewDto);
        studentGradeRepository.updateAttanceStartqr(reviewDto.classId());
        return noContent().build();
    }

    @GetMapping("review/student")
    public ResponseEntity<List<StudentReview>> getStudentForReviews(@RequestParam Integer classId) {
        return ResponseEntity.ok(userService.getStudentGratesByClassId(classId));

    }
    @PostMapping("stop/review")
    public  ResponseEntity<Void> stopReview(@RequestParam Integer classId){
        userService.stopReviewById(classId);
        return  ResponseEntity.noContent().build();
    }


}
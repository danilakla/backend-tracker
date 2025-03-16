package com.example.backendtracker.qrmanager.service;

import com.example.backendtracker.qrmanager.dto.ReviewDto;
import com.example.backendtracker.qrmanager.entity.ClassGeneral;
import com.example.backendtracker.qrmanager.entity.Review;
import com.example.backendtracker.qrmanager.entity.StudentReview;
import com.example.backendtracker.qrmanager.repository.ClassGeneralRepository;
import com.example.backendtracker.qrmanager.repository.ReviewRepository;
import com.example.backendtracker.qrmanager.repository.StudentReviewRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QRService {
    @Autowired
    private ClassGeneralRepository classGeneralRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private StudentReviewRepository studentReviewRepository;


    public ClassGeneral save(ClassGeneral keyInformationForQr) {
        return classGeneralRepository.save(keyInformationForQr);
    }

    @CacheEvict(value = "classgeneral", key = "#id")

    public void delete(Integer id) {
        classGeneralRepository.deleteById(id);
    }

    public void initProcessOfQrManagement(ReviewDto reviewDto) {
        reviewRepository.save(Review.builder()
                .expiration(reviewDto.expiration())
                .classId(reviewDto.classId()).build());

    }


    public void reviewStudent(Integer classId, Integer studentGradeId) throws BadRequestException {
        Long expr = getByIdReview(classId).getExpiration();
        studentReviewRepository.save(StudentReview.builder()
                .studentGradeId(studentGradeId)
                .classId(classId)
                .expiration(expr).build());

    }

    public List<StudentReview> getStudentGratesByClassId(Integer classId) {

        return studentReviewRepository.findAllByClassId(classId);
    }

    public ClassGeneral getById(Integer id) throws BadRequestException {
        return classGeneralRepository.findById(id).orElseThrow(BadRequestException::new);
    }


    @Cacheable(value = "review", key = "#id")
    public Review getByIdReview(Integer id) throws BadRequestException {
        return reviewRepository.findById(id).orElseThrow(BadRequestException::new);
    }

    @CacheEvict(value = "review", key = "#id")
    public void stopReviewById(Integer id) {
        reviewRepository.deleteById(id);
        studentReviewRepository.deleteAllByClassId(id);
    }
}
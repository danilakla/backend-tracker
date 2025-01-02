package com.example.backendtracker.qrmanager.repository;

import com.example.backendtracker.qrmanager.entity.Review;
import com.example.backendtracker.qrmanager.entity.StudentReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentReviewRepository extends CrudRepository<StudentReview, Integer> {

    List<StudentReview> findAllByClassId(Integer classId);
    void deleteAllByClassId(Integer classId);
}
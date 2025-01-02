package com.example.backendtracker.qrmanager.repository;

import com.example.backendtracker.qrmanager.entity.ClassGeneral;
import com.example.backendtracker.qrmanager.entity.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {
}
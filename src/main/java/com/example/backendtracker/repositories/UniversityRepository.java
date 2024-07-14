package com.example.backendtracker.repositories;

import com.example.backendtracker.models.University;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends CrudRepository<University, Integer> {

}
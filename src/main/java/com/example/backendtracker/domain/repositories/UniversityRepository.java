package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.University;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends CrudRepository<University, Integer> {

}
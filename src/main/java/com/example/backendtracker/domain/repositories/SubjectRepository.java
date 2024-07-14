package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Subject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Integer> {

}
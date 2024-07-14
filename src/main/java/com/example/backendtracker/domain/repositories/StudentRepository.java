package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {

}
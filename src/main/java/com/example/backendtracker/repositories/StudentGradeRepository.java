package com.example.backendtracker.repositories;

import com.example.backendtracker.models.StudentGrade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentGradeRepository extends CrudRepository<StudentGrade, Integer> {

}

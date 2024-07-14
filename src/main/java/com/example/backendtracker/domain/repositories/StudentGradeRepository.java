package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.StudentGrade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentGradeRepository extends CrudRepository<StudentGrade, Integer> {

}

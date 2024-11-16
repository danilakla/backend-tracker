package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.StudentGrade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentGradeRepository extends CrudRepository<StudentGrade, Integer> {

    List<StudentGrade> findAllByIdClass(int id);

    List<StudentGrade> findAllByIdClassInAndAndIdStudentIn(List<Integer> idClass, List<Integer> studentId);

}

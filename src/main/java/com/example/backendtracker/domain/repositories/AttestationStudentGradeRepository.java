package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.AttestationStudentGrade;
import com.example.backendtracker.domain.models.StudentGrade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttestationStudentGradeRepository extends CrudRepository<AttestationStudentGrade, Integer> {


    List<AttestationStudentGrade> findAllByIdClassInAndAndIdStudentIn(List<Integer> idClass, List<Integer> studentId);
    List<AttestationStudentGrade> findAllByIdClassAndIdStudentIn(Integer idClass, List<Integer> studentId);

}

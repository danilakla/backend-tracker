package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.AttestationStudentGrade;
import com.example.backendtracker.domain.models.StudentGrade;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttestationStudentGradeRepository extends CrudRepository<AttestationStudentGrade, Integer> {


    List<AttestationStudentGrade> findAllByIdClassInAndAndIdStudentIn(List<Integer> idClass, List<Integer> studentId);
    @Query("select * from attestationstudentgrades where id_class = :idclass and id_student in (:studentId)")
    List<AttestationStudentGrade> getattesifs(Integer idclass, List<Integer> studentId);

    @Modifying
    @Query("DELETE FROM attestationstudentgrades c WHERE c.id_class_group = :classGroupId")
    void deleteAllByIdClassGroup(Integer classGroupId);

}

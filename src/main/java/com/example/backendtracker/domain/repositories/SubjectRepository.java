package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Subject;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Integer> {
    @Modifying
    @Query("UPDATE Subjects SET id_teacher = :newTeacherId WHERE id_teacher = :oldTeacherId")
    void updateTeacherId(@Param("newTeacherId") int newTeacherId, @Param("oldTeacherId") int oldTeacherId);
}
package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.ClassGroup;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassGroupRepository extends CrudRepository<ClassGroup, Integer> {

    @Modifying
    @Query("UPDATE ClassGroups SET id_teacher = :newTeacherId WHERE id_teacher = :oldTeacherId")
    void updateTeacherId(@Param("newTeacherId") int newTeacherId, @Param("oldTeacherId") int oldTeacherId);

    @Modifying
    @Query("UPDATE ClassGroups SET id_dean = :newDeanId WHERE id_dean = :oldDeanId")
    void updateByDeanId(@Param("newDeanId") int newDeanId, @Param("oldDeanId") int oldDeanId);

    List<ClassGroup> findAllByIdDean(Integer deanId);

    Optional<ClassGroup> findByIdTeacherAndIdClassFormatAndAndIdSubject(int teacherId, int classFormatId, int subjectId);
}
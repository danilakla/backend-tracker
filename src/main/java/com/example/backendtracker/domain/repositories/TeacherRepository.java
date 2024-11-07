package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Dean;
import com.example.backendtracker.domain.models.Teacher;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Integer> {
    @Query(value = "SELECT * FROM Teachers WHERE id_account = :id_account")
    Optional<Teacher> findByIdAccount(@Param("id_account") Integer id_account);

    List<Teacher> findAllByIdUniversity(Integer id_university);

    Optional<Teacher> findByIdUniversityAndIdTeacher(Integer id_university, Integer id_teacher);
}
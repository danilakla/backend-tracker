package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Dean;
import com.example.backendtracker.domain.models.Student;
import com.example.backendtracker.domain.models.UserRole;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
    @Query(value = "SELECT * FROM Students WHERE id_account = :id_account")
    Optional<Student> findByIdAccount(@Param("id_account") Integer id_account);

    @Query(value = "SELECT * FROM students WHERE name = :name")
    Student findStudentByName(@Param("name") String name);

    Optional<Student> findStudentByKeyStudentParents(String parentsKey);

    List<Student> findAllByIdSubgroup(Integer idSubgroup);
}
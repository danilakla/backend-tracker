package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Subject;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Integer> {
    @Modifying
    @Query("UPDATE Subjects SET id_dean = :newDeanId WHERE id_dean = :oldDeanId")
    void updateDeanId(@Param("newDeanId") int newDeanId, @Param("oldDeanId") int oldDeanId);

    Optional<Subject> findByName(String name);
}
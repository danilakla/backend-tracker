package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Dean;
import com.example.backendtracker.domain.models.University;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityRepository extends CrudRepository<University, Integer> {
    @Query(value = "SELECT * FROM Universities WHERE id_admin = :id_admin")
    Optional<University> findByAdminId(@Param("id_admin") Integer id_admin);
}
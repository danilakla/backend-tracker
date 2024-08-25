package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Admin;
import com.example.backendtracker.domain.models.Dean;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeanRepository extends CrudRepository<Dean, Integer> {
    @Query(value = "SELECT * FROM Deans WHERE id_account = :id_account")
    Optional<Dean> findByIdAccount(@Param("id_account") Integer id_account);
}

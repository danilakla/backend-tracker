package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Specialty;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialtyRepository extends CrudRepository<Specialty, Integer> {
    @Query("SELECT * FROM Specialty WHERE idDean = :idDean")
    List<Specialty> findByDeanId(@Param("idDean") Integer idDean);

    List<Specialty> findAllByNameIn(List<String> names);

    Optional<Specialty> findByName(String name);


}
package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Specialty;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialtyRepository extends CrudRepository<Specialty, Integer> {
    @Query("SELECT * FROM Specialties WHERE id_dean = :idDean")
    List<Specialty> findByDeanId(@Param("idDean") Integer idDean);

    Optional<List<Specialty>> findAllByNameInAndIdDean(List<String> names, Integer idDean);

    Optional<Specialty> findByNameAndIdDean(String name, Integer deanId);

    @Modifying
    @Query("UPDATE Specialties SET id_dean = :newDeanId WHERE id_dean = :oldDeanId")
    void updateDeanId(@Param("newDeanId") int newDeanId, @Param("oldDeanId") int oldDeanId);
}
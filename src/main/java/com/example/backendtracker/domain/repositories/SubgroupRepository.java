package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Subgroup;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubgroupRepository extends CrudRepository<Subgroup, Integer> {

    List<Subgroup> findAllByIdDean(Integer idDean);

    @Modifying
    @Query("UPDATE Subgroups SET id_dean = :newDeanId WHERE id_dean = :oldDeanId")
    void updateDeanId(@Param("newDeanId") int newDeanId, @Param("oldDeanId") int oldDeanId);

    List<Subgroup> findAllByIdSubgroupIn(List<Integer> idSubgroupIn);
}
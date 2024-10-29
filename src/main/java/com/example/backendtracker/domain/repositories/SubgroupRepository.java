package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Subgroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubgroupRepository extends CrudRepository<Subgroup, Integer> {

    List<Subgroup> findAllByIdDean(Integer idDean);
}
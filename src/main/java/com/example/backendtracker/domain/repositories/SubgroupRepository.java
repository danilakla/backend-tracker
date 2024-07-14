package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Subgroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubgroupRepository extends CrudRepository<Subgroup, Integer> {

}
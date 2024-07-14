package com.example.backendtracker.repositories;

import com.example.backendtracker.models.Subgroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubgroupRepository extends CrudRepository<Subgroup, Integer> {

}
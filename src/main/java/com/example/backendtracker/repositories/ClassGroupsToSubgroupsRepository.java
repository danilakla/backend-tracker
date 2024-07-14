package com.example.backendtracker.repositories;

import com.example.backendtracker.models.ClassGroupsToSubgroups;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassGroupsToSubgroupsRepository extends CrudRepository<ClassGroupsToSubgroups, Integer> {

}

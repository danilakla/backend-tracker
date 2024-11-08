package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.ClassGroupsToSubgroups;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassGroupsToSubgroupsRepository extends CrudRepository<ClassGroupsToSubgroups, Integer> {

    List<ClassGroupsToSubgroups> findAllByIdClassGroup(Integer classGroupId);
}

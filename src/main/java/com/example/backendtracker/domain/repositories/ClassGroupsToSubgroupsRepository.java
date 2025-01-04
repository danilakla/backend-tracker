package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.ClassGroupsToSubgroups;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassGroupsToSubgroupsRepository extends CrudRepository<ClassGroupsToSubgroups, Integer> {

    List<ClassGroupsToSubgroups> findAllByIdClassGroup(Integer classGroupId);

    List<ClassGroupsToSubgroups> findAllByIdClassHold(Integer idHold);

    @Modifying
    @Query("DELETE FROM ClassGroupsToSubgroups c WHERE c.id_class_group = :classGroupId")
    void deleteAllByIdClassGroup(Integer classGroupId);

    List<ClassGroupsToSubgroups> findAllByIdClassGroupAndAndIdSubgroupIn(Integer idClassGroup, List<Integer> IdSubgroups);


}

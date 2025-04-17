package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Classes;
import com.example.backendtracker.entities.dean.dto.ClassDto;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends CrudRepository<Classes, Integer> {
    List<Classes> findAllByIdClassHold(Integer idClassHold);

    @Query(value = "SELECT c.id_class AS idClass, c.is_attestation AS isAttestation " +
            "FROM ClassGroupsToSubgroups cgts " +
            "JOIN Classes c ON cgts.id_class_hold = c.id_class_hold " +
            "WHERE cgts.id_subgroup = :subgroupId",rowMapperClass = ClassDto.ClassDtoRowMapper.class)
    List<ClassDto> findClassIdsBySubgroupId(Integer subgroupId);

}

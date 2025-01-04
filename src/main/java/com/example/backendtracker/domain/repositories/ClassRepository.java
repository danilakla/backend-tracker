package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Classes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends CrudRepository<Classes, Integer> {
    List<Classes> findAllByIdClassHold(Integer idClassHold);

}

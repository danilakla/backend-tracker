package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Discipline;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplineRepository extends CrudRepository<Discipline, Integer> {

}
package com.example.backendtracker.repositories;

import com.example.backendtracker.models.Discipline;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplineRepository extends CrudRepository<Discipline, Integer> {

}
package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Class;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends CrudRepository<Class, Integer> {

}

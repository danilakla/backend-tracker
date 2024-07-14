package com.example.backendtracker.repositories;

import com.example.backendtracker.models.Class;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends CrudRepository<Class, Integer> {

}

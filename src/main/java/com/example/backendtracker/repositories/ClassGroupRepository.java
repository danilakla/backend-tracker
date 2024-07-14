package com.example.backendtracker.repositories;

import com.example.backendtracker.models.ClassGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassGroupRepository extends CrudRepository<ClassGroup, Integer> {

}

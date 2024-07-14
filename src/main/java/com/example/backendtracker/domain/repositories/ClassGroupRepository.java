package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.ClassGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassGroupRepository extends CrudRepository<ClassGroup, Integer> {

}

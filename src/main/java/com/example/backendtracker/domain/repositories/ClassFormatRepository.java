package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.ClassFormat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassFormatRepository extends CrudRepository<ClassFormat, Integer> {

}

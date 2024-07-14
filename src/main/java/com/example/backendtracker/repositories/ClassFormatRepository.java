package com.example.backendtracker.repositories;

import com.example.backendtracker.models.ClassFormat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassFormatRepository extends CrudRepository<ClassFormat, Integer> {

}

package com.example.backendtracker.repositories;

import com.example.backendtracker.models.Dean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeanRepository extends CrudRepository<Dean, Integer> {

}

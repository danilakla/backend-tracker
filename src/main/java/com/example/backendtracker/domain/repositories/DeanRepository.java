package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Dean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeanRepository extends CrudRepository<Dean, Integer> {

}

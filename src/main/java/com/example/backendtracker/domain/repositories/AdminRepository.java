package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Admin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {

}

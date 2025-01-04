package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.ClassGroup;
import com.example.backendtracker.domain.models.ClassGroupsHold;
import com.example.backendtracker.domain.models.UserRole;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassGroupsHoldRepository extends CrudRepository<ClassGroupsHold, Integer> {
}

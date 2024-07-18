package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.UserAccount;
import com.example.backendtracker.domain.models.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {
}

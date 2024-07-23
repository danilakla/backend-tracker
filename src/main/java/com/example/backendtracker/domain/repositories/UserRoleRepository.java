package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.UserAccount;
import com.example.backendtracker.domain.models.UserRole;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {
    @Query(value = "SELECT * FROM UserRoles WHERE role_name = :roleName")

    Optional<UserRole> findByRoleName(String roleName);
}

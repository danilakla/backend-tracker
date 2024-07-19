package com.example.backendtracker.security.service;

import com.example.backendtracker.domain.models.UserRole;
import com.example.backendtracker.domain.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RoleService {
    public final UserRoleRepository userRoleRepository;

    @Autowired
    public RoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public Integer getRoleIdByRoleName(String roleName) {
        return finedRoleByRoleName(roleName)
                .orElseThrow(() -> new NoSuchElementException("role: " + roleName + "is not founded")
                ).getIdRole();

    }

    public Optional<UserRole> finedRoleByRoleName(String roleName) {

        return userRoleRepository.findByRoleName(roleName);
    }


}

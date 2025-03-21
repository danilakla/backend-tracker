package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.Admin;
import com.example.backendtracker.domain.models.UserAccount;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {
    @Query(value = "SELECT * FROM Admins WHERE id_account = :id_account")
    Optional<Admin> findByIdAccount(@Param("id_account") Integer id_account);

}

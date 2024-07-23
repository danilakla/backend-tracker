package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.UserAccount;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Integer> {
    @Query(value = "SELECT * FROM UserAccounts WHERE login = :login")
    Optional<UserAccount> findByLogin(@Param("login") String login);
}

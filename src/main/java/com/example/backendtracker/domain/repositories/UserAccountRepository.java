package com.example.backendtracker.domain.repositories;

import com.example.backendtracker.domain.models.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Integer> {
    Optional<UserAccount> findByLogin(String login);
}

package com.example.backendtracker.security.util;

import com.example.backendtracker.domain.models.UserAccount;
import com.example.backendtracker.domain.models.UserRole;
import com.example.backendtracker.domain.repositories.UserAccountRepository;
import com.example.backendtracker.domain.repositories.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@AllArgsConstructor
@Component
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    private final UserRoleRepository userRoleRepository;


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {


        UserAccount userAccount = userAccountRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("There's no user with login: " + login));
        String userRole = userRoleRepository.findById(userAccount.getIdRole()).orElseThrow(NoSuchElementException::new).getRoleName();
        return User.
                builder()
                .username(userAccount.getLogin())
                .password(userAccount.getPassword())
                .authorities(userRole).build();
    }
}

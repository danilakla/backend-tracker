package com.example.backendtracker.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

//TODO REVIEW AFTER INIT ENTITY FOR ALL PROJECT
@Component
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = jdbcTemplate.query("select * from t_user_teacher where c_username = ?", (rs, i) ->
                        User.builder().
                                username(rs.getString(2))
                                .password(rs.getString(3))
                                .authorities("ROLE_Teacher").build(), username)
                .stream().findFirst().orElse(null);
        if (Objects.isNull(userDetails)) {
             userDetails = jdbcTemplate.query("select * from t_user_student where  c_username = ?", (rs, i) ->
                            User.builder().
                                    username(rs.getString(2))
                                    .password(rs.getString(3))
                                        .authorities("ROLE_Student").build(), username)
                    .stream().findFirst().orElse(null);
        }

        return userDetails;
    }
}

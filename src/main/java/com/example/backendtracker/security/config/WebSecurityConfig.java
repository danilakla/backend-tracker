package com.example.backendtracker.security.config;


import com.example.backendtracker.security.filter.JwtRequestFilter;
import com.example.backendtracker.security.util.JwtService;
import com.example.backendtracker.security.util.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;
//TODO REVIEW AFTER INIT ENTITY FOR ALL PROJECT

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    @Autowired
    private SecurityUserDetailsService securityUserDetailsService;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(PasswordEncoder passwordEncoder) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(securityUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/teacher").hasRole("Teacher")
                        .requestMatchers("/dean/**").hasRole("Dean")
                        .requestMatchers("/admin/**").permitAll()

                        .requestMatchers("/authenticate").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/student").hasRole("Student")
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/swagger-ui/index.html",
                                "/favicon-32x32.png",
                                "/favicon-16x16.png").permitAll()

                        .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.setAllowedMethods(List.of("*"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowedOriginPatterns(List.of("*"));
                    return config;
                }));


        return http.build();
    }

}
package com.example.backendtracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

//TODO REVIEW AFTER INIT ENTITY FOR ALL PROJECT

@Configuration
public class JdbcConfig {

    @Value("${spring.datasource.driver-class-name}")
    private  String driver;
    @Value("${spring.datasource.url}")
    private  String url;
    @Value("${spring.datasource.password}")
    private  String password;

    @Value("${spring.datasource.username}")
    private  String username;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
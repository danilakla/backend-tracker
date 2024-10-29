package com.example.backendtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

//todo admin -recovery password and delete dean + teacher
//todo dean - get list of student and subgroup recovery delete add
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BackendTrackerApplication {
    public static void main(String[] args) {

        SpringApplication.run(BackendTrackerApplication.class, args);
    }
}

package com.example.backendtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
//
//todo 1.) studentgrade table changed - add new prop, lab_is_passed or not, extend modal where update student grade
//todo 2.) classes table changed - add bew prop is_attestation or not
//todo 3.) add new table attestationstudentgrade
//todo 4.) also have to review endpoint which return classes and studentgrate in order to avoid missing of the new props
//todo 5.) also develop 6 controller, 1.create attestation 2.notify teacher about attes 3.notify dean about
//todo. teacher how hasn't attested yet 4. allow teacher attest a class  5.update attesta_student_grate manualy
//todo. 6. calculate avg attestation
//todo review commit in order to verify all changes

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BackendTrackerApplication {
    public static void main(String[] args) {

        SpringApplication.run(BackendTrackerApplication.class, args);
    }
}

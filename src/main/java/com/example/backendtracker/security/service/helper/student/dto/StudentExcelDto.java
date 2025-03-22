package com.example.backendtracker.security.service.helper.student.dto;


import java.time.LocalDate;

public record StudentExcelDto(String name, String lastname, String surname, String numberOfGroup, String specialty,
                              LocalDate date ) {

}

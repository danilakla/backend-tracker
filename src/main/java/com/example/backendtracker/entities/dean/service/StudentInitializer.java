package com.example.backendtracker.entities.dean.service;

import com.example.backendtracker.domain.models.Specialty;
import com.example.backendtracker.domain.repositories.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentInitializer {
    private final SpecialtyRepository specialtyRepository;


    public void createStudentsAccount(Integer deanId) {

//        Specialty specialty =
    }


    public void createSpecialties(Integer deanId, String name) {
        specialtyRepository.save(Specialty.builder().name(name)
                .idDean(deanId).build());

    }
}

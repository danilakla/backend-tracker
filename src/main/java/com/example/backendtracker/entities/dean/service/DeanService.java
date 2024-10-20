package com.example.backendtracker.entities.dean.service;

import com.example.backendtracker.domain.models.Specialty;
import com.example.backendtracker.domain.repositories.SpecialtyRepository;
import com.example.backendtracker.entities.dean.dto.CreateSpecialtyDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeanService {
    private final SpecialtyRepository specialtyRepository;


    public void createSpecialty(CreateSpecialtyDto specialtyDto, Integer accountId) {
        checkExistenceOfSpecialty(specialtyDto.name());
        specialtyRepository.save(Specialty.builder()
                .name(specialtyDto.name())
                .idDean(accountId).build());

    }

    public void checkExistenceOfSpecialty(String name) {
        specialtyRepository.findByName(name).ifPresent(specialty -> {
            throw new RuntimeException("The specialty already exists");
        });

    }
}

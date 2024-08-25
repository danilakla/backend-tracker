package com.example.backendtracker.entities.admin.service;

import com.example.backendtracker.domain.models.University;
import com.example.backendtracker.domain.repositories.UniversityRepository;
import com.example.backendtracker.entities.admin.dto.UniversityCreateDto;
import com.example.backendtracker.security.exception.InvalidEncryptionProcessException;
import com.example.backendtracker.security.util.SecretDataUtil;
import com.example.backendtracker.util.AccountInformationRetriever;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {
    private final UniversityRepository universityRepository;
    private final SecretDataUtil secretDataUtil;

    public String generateKeyBasedOnUniversityId(Integer adminId) throws InvalidEncryptionProcessException {
        University university = universityRepository.findByAdminId(adminId)
                .orElseThrow(() -> new RuntimeException("There's no university"));
        return secretDataUtil.encrypt(String.valueOf(university.getIdUniversity()));
    }

    public void createUniversity(UniversityCreateDto universityCreateDto, Integer adminId) {
        universityRepository.save(University.builder()
                .idAdmin(adminId)
                .name(universityCreateDto.getName())
                .description(universityCreateDto.getDescription())
                .build());
    }
}

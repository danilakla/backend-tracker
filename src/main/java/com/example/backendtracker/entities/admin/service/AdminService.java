package com.example.backendtracker.entities.admin.service;

import com.example.backendtracker.domain.models.University;
import com.example.backendtracker.domain.repositories.UniversityRepository;
import com.example.backendtracker.entities.admin.dto.UniversityCreateDto;
import com.example.backendtracker.security.util.SecretDataUtil;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {
    private final UniversityRepository universityRepository;
    private final SecretDataUtil secretDataUtil;

    public String generateKeyBasedOnUniversityId(Integer adminId,String role) throws Exception {
        University university = universityRepository.findByAdminId(adminId)
                .orElseThrow(() -> new BadRequestException("There's no university"));
        return secretDataUtil.encrypt(university.getIdUniversity()+"%"+role);
    }

    public void createUniversity(UniversityCreateDto universityCreateDto, Integer adminId)  {


         universityRepository.findByAdminId(adminId).ifPresent(university -> {throw new RuntimeException("The university already exists");});
        universityRepository.save(University.builder()
                .idAdmin(adminId)
                .name(universityCreateDto.getName())
                .description(universityCreateDto.getDescription())
                .build());
    }

}

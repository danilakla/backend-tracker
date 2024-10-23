package com.example.backendtracker.entities.admin.service;

import com.example.backendtracker.domain.models.University;
import com.example.backendtracker.domain.repositories.UniversityRepository;
import com.example.backendtracker.entities.admin.dto.UniversityCreateDto;
import com.example.backendtracker.security.util.SecretDataUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class AdminService {
    private final UniversityRepository universityRepository;
    private final SecretDataUtil secretDataUtil;

    public String generateKey(Integer adminId, String... data) throws Exception {
        University university = universityRepository.findByAdminId(adminId)
                .orElseThrow(() -> new BadRequestException("There's no university"));
        StringBuilder value = new StringBuilder();
        Arrays.stream(data).forEach(str -> value.append(str + "%"));

        return secretDataUtil.encrypt(university.getIdUniversity() + "%" + StringUtils.substring(value.toString(), 0, value.length() - 1));
    }

    public void createUniversity(UniversityCreateDto universityCreateDto, Integer adminId) {


        universityRepository.findByAdminId(adminId).ifPresent(university -> {
            throw new RuntimeException("The university already exists");
        });
        universityRepository.save(University.builder()
                .idAdmin(adminId)
                .name(universityCreateDto.getName())
                .description(universityCreateDto.getDescription())
                .build());
    }

}

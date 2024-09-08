package com.example.backendtracker.security.service.helper;

import com.example.backendtracker.domain.models.Teacher;
import com.example.backendtracker.domain.repositories.TeacherRepository;
import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.service.UserInitializer;
import com.example.backendtracker.security.service.data.UserStoringKeys;
import com.example.backendtracker.security.util.SecretDataUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeacherInitializer implements UserInitializer {

    private final TeacherRepository teacherRepository;
    private final SecretDataUtil secretDataUtil;

    @Override
    public void init(String role, UserStoringKeys userStoringKeys) throws InvalidEncryptedDataException {
        Integer universityId = getUniversityId(secretDataUtil.decrypt(userStoringKeys.secKey()), role);

        teacherRepository.save(Teacher
                .builder()
                .idUniversity(universityId)
                .idAccount(userStoringKeys.idAccount()).build());
    }
}

package com.example.backendtracker.security.service.helper;

import com.example.backendtracker.domain.models.Teacher;
import com.example.backendtracker.domain.repositories.TeacherRepository;
import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.service.UserInitializer;
import com.example.backendtracker.security.service.data.UserStoringKeys;
import com.example.backendtracker.security.util.SecretDataUtil;
import com.example.backendtracker.util.NameConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeacherInitializer implements UserInitializer {

    private final TeacherRepository teacherRepository;
    private final SecretDataUtil secretDataUtil;

    @Override
    public void init(UserRegistrationRequestDTO userInfo, UserStoringKeys userStoringKeys) throws InvalidEncryptedDataException {
        Integer universityId = getUniversityId(secretDataUtil.decrypt(userStoringKeys.secKey()), userInfo.role());

        teacherRepository.save(Teacher
                .builder()
                .idUniversity(universityId)
                .flpName(NameConverter.convertNameToDb(userInfo.lastname(), userInfo.name(), userInfo.surname()))
                .idAccount(userStoringKeys.idAccount()).build());
    }
}

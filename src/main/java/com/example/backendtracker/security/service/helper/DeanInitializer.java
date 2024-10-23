package com.example.backendtracker.security.service.helper;

import com.example.backendtracker.domain.models.Dean;
import com.example.backendtracker.domain.repositories.DeanRepository;
import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.service.UserInitializer;
import com.example.backendtracker.security.service.data.UserStoringKeys;
import com.example.backendtracker.security.util.SecretDataUtil;
import com.example.backendtracker.util.NameConverter;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeanInitializer implements UserInitializer {


    private final SecretDataUtil secretDataUtil;
    private final DeanRepository deanRepository;

    @Override
    public void init(UserRegistrationRequestDTO userInfo, UserStoringKeys userStoringKeys) throws InvalidEncryptedDataException {

        Integer universityId = getUniversityId(secretDataUtil.decrypt(userStoringKeys.secKey()), userInfo.role());
        String faculty = getFaculty(secretDataUtil.decrypt(userStoringKeys.secKey()));
        deanRepository.save(Dean.builder()
                .idUniversity(universityId)
                .faculty(faculty)
                .flpName(NameConverter.convertNameToDb(userInfo.lastname(), userInfo.name(), userInfo.surname()))
                .idAccount(userStoringKeys.idAccount()).build());

    }


}

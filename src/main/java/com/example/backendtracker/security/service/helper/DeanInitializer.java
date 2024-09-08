package com.example.backendtracker.security.service.helper;

import com.example.backendtracker.domain.models.Dean;
import com.example.backendtracker.domain.repositories.DeanRepository;
import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.service.UserInitializer;
import com.example.backendtracker.security.service.data.UserStoringKeys;
import com.example.backendtracker.security.util.SecretDataUtil;
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
    public void init(String role, UserStoringKeys userStoringKeys) throws InvalidEncryptedDataException {

        Integer universityId = getUniversityId(secretDataUtil.decrypt(userStoringKeys.secKey()), role);

        deanRepository.save(Dean.builder()
                .idUniversity(universityId)
                .idAccount(userStoringKeys.idAccount()).build());

    }


}

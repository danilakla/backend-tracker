package com.example.backendtracker.security.service.helper;

import com.example.backendtracker.domain.models.Dean;
import com.example.backendtracker.domain.repositories.DeanRepository;
import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.service.UserInitializer;
import com.example.backendtracker.security.service.data.UserStoringKeys;
import com.example.backendtracker.security.util.SecretDataUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeanInitializer implements UserInitializer {


    private final SecretDataUtil secretDataUtil;
    private final DeanRepository deanRepository;

    @Override
    public void init(UserStoringKeys userStoringKeys) throws InvalidEncryptedDataException {
        Integer idUniversity = Integer.valueOf(secretDataUtil.decrypt(userStoringKeys.secKey()));
        deanRepository.save(Dean.builder()
                .idUniversity(idUniversity)
                .idAccount(userStoringKeys.idAccount()).build());
    }
}

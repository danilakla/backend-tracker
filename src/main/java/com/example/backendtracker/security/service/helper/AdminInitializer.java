package com.example.backendtracker.security.service.helper;

import com.example.backendtracker.domain.models.Admin;
import com.example.backendtracker.domain.repositories.AdminRepository;
import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.service.UserInitializer;
import com.example.backendtracker.security.service.data.UserStoringKeys;
import com.example.backendtracker.security.util.SecretDataUtil;
import com.example.backendtracker.util.NameConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AdminInitializer implements UserInitializer {
    private final AdminRepository adminRepository;

    @Override
    public void init(UserRegistrationRequestDTO userInfo, UserStoringKeys userStoringKeys) throws InvalidEncryptedDataException {
        Admin admin = Admin.builder().idAccount((userStoringKeys.idAccount()))
                .flpName(NameConverter.convertNameToDb(userInfo.lastname(), userInfo.name(), userInfo.surname())).build();
        adminRepository.save(admin);

    }
}

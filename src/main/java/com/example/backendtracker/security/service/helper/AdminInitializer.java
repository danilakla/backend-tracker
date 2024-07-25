package com.example.backendtracker.security.service.helper;

import com.example.backendtracker.domain.models.Admin;
import com.example.backendtracker.domain.repositories.AdminRepository;
import com.example.backendtracker.security.service.UserInitializer;
import com.example.backendtracker.security.service.data.UserStoringKeys;
import com.example.backendtracker.security.util.SecretDataUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AdminInitializer implements UserInitializer {
    private final AdminRepository adminRepository;

    @Override
    public void init(UserStoringKeys userStoringKeys) throws Exception {
        Admin admin = Admin.builder().idAccount((userStoringKeys.idAccount())).build();
        adminRepository.save(admin);

    }
}

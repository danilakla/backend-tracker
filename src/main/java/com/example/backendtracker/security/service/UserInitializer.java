package com.example.backendtracker.security.service;

import com.example.backendtracker.reliability.exception.BadRequestException;
import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.service.data.UserStoringKeys;

public interface UserInitializer {
    void init(UserRegistrationRequestDTO userRegistrationRequest, UserStoringKeys userStoringKeys) throws InvalidEncryptedDataException;

    default Integer getUniversityId(String encrypted, String role) {
        String[] arrEncryptedInformation = encrypted.split("%");
        checkRoleConsistency(arrEncryptedInformation[1], role);
        return Integer.valueOf(arrEncryptedInformation[0]);
    }


    default String getFaculty(String encrypted) {
        String[] arrEncryptedInformation = encrypted.split("%");
        return arrEncryptedInformation[2];
    }

    default void checkRoleConsistency(String roleFromKey, String roleUser) {

        if (!roleFromKey.equals(roleUser)) {
            throw new BadRequestException("Check you role, Key must be applied to different role");
        }
    }
}

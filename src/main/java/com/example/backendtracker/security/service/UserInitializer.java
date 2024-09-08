package com.example.backendtracker.security.service;

import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.service.data.UserStoringKeys;

public interface UserInitializer {
     void init(String role, UserStoringKeys userStoringKeys) throws InvalidEncryptedDataException;
     default Integer getUniversityId(String encrypted, String role) {
          String[] arrEncryptedInformation = encrypted.split("%");
          checkRoleConsistency(arrEncryptedInformation[1], role);
          return Integer.valueOf(arrEncryptedInformation[0]);
     }

     default void checkRoleConsistency(String roleFromKey, String roleUser) {

          if (!roleFromKey.equals(roleUser)) {
               throw new RuntimeException("Check you role, Key must be applied to different role");
          }
     }
}

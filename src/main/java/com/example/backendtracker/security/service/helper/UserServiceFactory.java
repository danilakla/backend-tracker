package com.example.backendtracker.security.service.helper;

import com.example.backendtracker.security.dto.UserRegistrationRequestDTO;
import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.service.UserInitializer;
import com.example.backendtracker.security.service.data.UserStoringKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class UserServiceFactory {

    private final Map<String, UserInitializer> serviceMap;

    @Autowired
    public UserServiceFactory(List<UserInitializer> services) {
        serviceMap = new HashMap<>();
        for (UserInitializer service : services) {
            if (service instanceof AdminInitializer) {
                serviceMap.put("ADMIN", service);
            } else if (service instanceof TeacherInitializer) {
                serviceMap.put("TEACHER", service);
            } else if (service instanceof DeanInitializer) {
                serviceMap.put("DEAN", service);
            }
        }
    }

    public void initUser(UserStoringKeys userStoringKeys, UserRegistrationRequestDTO userRegistrationRequest) throws InvalidEncryptedDataException {
         serviceMap.get(userRegistrationRequest.role().toUpperCase()).init(userRegistrationRequest,userStoringKeys);
    }
}

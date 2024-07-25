package com.example.backendtracker.security.service;

import com.example.backendtracker.security.service.data.UserStoringKeys;

public interface UserInitializer {
     void init(UserStoringKeys userStoringKeys) throws Exception;
}

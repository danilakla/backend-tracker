package com.example.backendtracker.security.util;

public interface UserPasswordManager {
    public String encode(String password);
    boolean matches(String password, String encodedPassword);
}

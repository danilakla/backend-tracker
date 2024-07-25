package com.example.backendtracker.security.util;


public interface SecretDataUtil {
    public String encrypt(String data) throws Exception;

    public String decrypt(String encryptedData) throws Exception;
}

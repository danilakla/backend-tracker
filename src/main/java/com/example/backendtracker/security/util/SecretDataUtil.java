package com.example.backendtracker.security.util;


import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.exception.InvalidEncryptionProcessException;

public interface SecretDataUtil {
    public String encrypt(String data) throws InvalidEncryptionProcessException;

    public String decrypt(String encryptedData) throws InvalidEncryptedDataException;
}

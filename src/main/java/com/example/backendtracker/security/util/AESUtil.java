package com.example.backendtracker.security.util;

import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.exception.InvalidEncryptionProcessException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Log4j2
public class AESUtil implements SecretDataUtil {

    private final SecretKeySpec secretKey;
    @Value("${encryption.algorithm}")
    private String algorithm;

    public AESUtil(SecretKeySpec secretKey) {
        this.secretKey = secretKey;
    }

    public String encrypt(String data) throws InvalidEncryptionProcessException {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new InvalidEncryptionProcessException("Invalid configuration for encryption");
        }

    }

    public String decrypt(String encryptedData) throws InvalidEncryptedDataException {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new InvalidEncryptedDataException("Please, could you check the secure key");
        }

    }
}

package com.example.backendtracker.security.config;

import com.example.backendtracker.security.util.AESUtil;
import com.example.backendtracker.security.util.SecretDataUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class EncryptionConfig {

    @Value("${encryption.key}")
    private String encryptionKey;
    @Value("${encryption.algorithm}")
    private String algorithm;
    @Bean
    public SecretKeySpec secretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(encryptionKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm);
    }

    @Bean
    public SecretDataUtil secretDataUtilUtil(SecretKeySpec secretKey) {
        return new AESUtil(secretKey);
    }
}
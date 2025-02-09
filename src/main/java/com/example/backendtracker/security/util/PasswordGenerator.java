package com.example.backendtracker.security.util;

import org.apache.commons.lang3.RandomStringUtils;

public class PasswordGenerator {

    public static String generatePassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }
    public static String generateParentPassword() {
        return RandomStringUtils.randomAlphabetic(8);
    }
}
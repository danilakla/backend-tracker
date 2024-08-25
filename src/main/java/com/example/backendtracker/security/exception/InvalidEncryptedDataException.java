package com.example.backendtracker.security.exception;

public class InvalidEncryptedDataException  extends Exception {
    public InvalidEncryptedDataException(String message) {
        super(message);
    }
}
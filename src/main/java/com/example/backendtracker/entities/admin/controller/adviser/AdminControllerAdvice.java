package com.example.backendtracker.entities.admin.controller.adviser;

import com.example.backendtracker.security.exception.InvalidEncryptedDataException;
import com.example.backendtracker.security.exception.InvalidEncryptionProcessException;
import com.example.backendtracker.security.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
@RequiredArgsConstructor
public class AdminControllerAdvice {


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerBadCredentialsForRegistration(RuntimeException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }


    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerBadCredentialsForRegistration(BadRequestException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }




    @ExceptionHandler(InvalidEncryptionProcessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleInvalidEncryptionProcess(InvalidEncryptionProcessException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }


}


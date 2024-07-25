package com.example.backendtracker.security.controller.advicer;

import com.example.backendtracker.security.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;

@ControllerAdvice
@RequiredArgsConstructor
public class AuthenticationControllerAdvice {


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handlerBadCredentialsForRegistration(UserAlreadyExistsException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler( NoSuchElementException.class)
    public ResponseEntity<String> handlerBadCredentialsForRegistration(NoSuchElementException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleUnAuthorizedUser(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }
}


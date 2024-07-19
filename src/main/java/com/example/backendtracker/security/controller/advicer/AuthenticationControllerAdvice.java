package com.example.backendtracker.security.controller.advicer;

import com.example.backendtracker.security.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;

@ControllerAdvice
@RequiredArgsConstructor
public class AuthenticationControllerAdvice {


    @ExceptionHandler({UserAlreadyExistsException.class, NoSuchElementException.class})
    public ResponseEntity<HttpClientErrorException.BadRequest> handlerBadCredentialsForRegistration() {

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<HttpClientErrorException.Unauthorized> handleUnAuthorizedUser() {
        return ResponseEntity.noContent().build();
    }
}


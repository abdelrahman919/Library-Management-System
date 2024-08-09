package com.maids.cc.Library_Management_System.exception_handle.handlers;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationExcHandler {

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<String> handleAuthenticationExc(InternalAuthenticationServiceException e) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(401))
                .body(e.getMessage());
    }


}

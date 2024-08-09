package com.maids.cc.Library_Management_System.exception_handle.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleControllerValidationExceptions(MethodArgumentNotValidException e) {

        StringBuilder errorMessagesBuilder = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(objectError -> {
            errorMessagesBuilder.append(objectError.getDefaultMessage());
            errorMessagesBuilder.append(System.lineSeparator());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessagesBuilder.toString());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleSqlExceptions(SQLIntegrityConstraintViolationException e) {

        String responseMessage = "Invalid input";

        String errorMessage = e.getMessage();
        if (errorMessage.contains("unique_name")) {
            responseMessage = "User name must be unique";
        }
        return ResponseEntity.badRequest().body(responseMessage);

    }


}

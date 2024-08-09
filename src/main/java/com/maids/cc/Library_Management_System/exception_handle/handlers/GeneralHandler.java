package com.maids.cc.Library_Management_System.exception_handle.handlers;

import com.maids.cc.Library_Management_System.exception_handle.custom_exceptions.BorrowValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class GeneralHandler {
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<String> handleInternalServerExc(HttpServerErrorException.InternalServerError e) {
        //Can alternatively use Aspects to log the stack trace
        e.printStackTrace();

        return ResponseEntity
                .internalServerError()
                .body("Some thing went wrong");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handeMsgNotReadable(HttpMessageNotReadableException e) {
        return ResponseEntity
                .badRequest()
                .body("Invalid data");
    }

    @ExceptionHandler(value = BorrowValidationException.class)
    public ResponseEntity<String> handleBorrowingValidationExceptions(BorrowValidationException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundExceptions(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }





}

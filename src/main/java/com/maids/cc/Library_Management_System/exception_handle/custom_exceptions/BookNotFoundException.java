package com.maids.cc.Library_Management_System.exception_handle.custom_exceptions;

import jakarta.persistence.EntityNotFoundException;

public class BookNotFoundException extends EntityNotFoundException {

    private static final String DEFAULT_MESSAGE =
            "Book was not found";

    public BookNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
    public BookNotFoundException(String message) {
        super(message);
    }

}

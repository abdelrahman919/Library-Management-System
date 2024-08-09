package com.maids.cc.Library_Management_System.exception_handle.custom_exceptions;

import jakarta.persistence.EntityNotFoundException;

public class PatronNotFoundException extends EntityNotFoundException {

    private static final String DEFAULT_MESSAGE =
            "Patron was not found";

    public PatronNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
    public PatronNotFoundException(String message) {
        super(message);
    }

}

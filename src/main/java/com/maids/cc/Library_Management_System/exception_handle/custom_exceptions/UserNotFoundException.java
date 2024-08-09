package com.maids.cc.Library_Management_System.exception_handle.custom_exceptions;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    private static final String DEFAULT_MESSAGE =
            "User was not found";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}

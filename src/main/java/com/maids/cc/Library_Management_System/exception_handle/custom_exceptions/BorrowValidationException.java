package com.maids.cc.Library_Management_System.exception_handle.custom_exceptions;

public class BorrowValidationException extends RuntimeException{
    private static final String DEFAULT_MESSAGE =
            "Borrow process is not valid";

    public BorrowValidationException() {
        super(DEFAULT_MESSAGE);
    }
    public BorrowValidationException(String message) {
        super(message);
    }
}

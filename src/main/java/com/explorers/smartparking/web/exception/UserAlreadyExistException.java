package com.explorers.smartparking.web.exception;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(final String message) {
        super(message);
    }
}

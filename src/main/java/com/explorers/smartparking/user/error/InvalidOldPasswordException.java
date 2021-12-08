package com.explorers.smartparking.user.error;

public class InvalidOldPasswordException extends RuntimeException {

    public InvalidOldPasswordException(String message) {
        super(message);
    }
}

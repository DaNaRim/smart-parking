package com.explorers.smartparking.user.error;

public class InvalidBalanceException extends RuntimeException {

    public InvalidBalanceException(String message) {
        super(message);
    }
}

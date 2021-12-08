package com.explorers.smartparking.parking.error;

public class NoEnoughMoneyException extends RuntimeException {

    public NoEnoughMoneyException(String message) {
        super(message);
    }
}

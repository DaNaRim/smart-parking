package com.explorers.smartparking.parking.exception;

public class NoEnoughMoneyException extends RuntimeException {

    public NoEnoughMoneyException(String message) {
        super(message);
    }
}

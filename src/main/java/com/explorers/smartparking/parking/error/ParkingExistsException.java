package com.explorers.smartparking.parking.error;

public class ParkingExistsException extends RuntimeException {

    public ParkingExistsException(String message) {
        super(message);
    }
}

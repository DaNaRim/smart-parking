package com.explorers.smartparking.parking.exception;

public class ParkingPlaceNotFoundException extends RuntimeException {

    public ParkingPlaceNotFoundException(String message) {
        super(message);
    }
}

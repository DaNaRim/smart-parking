package com.explorers.smartparking.parking.error;

public class ParkingPlaceNotFoundException extends RuntimeException {

    public ParkingPlaceNotFoundException(String message) {
        super(message);
    }
}

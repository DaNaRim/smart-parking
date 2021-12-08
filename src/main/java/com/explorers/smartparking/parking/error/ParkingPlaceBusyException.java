package com.explorers.smartparking.parking.error;

public class ParkingPlaceBusyException extends RuntimeException {

    public ParkingPlaceBusyException(String message) {
        super(message);
    }
}

package com.explorers.smartparking.parking.error;

public class ParkingPlaceNotBusyException extends RuntimeException {

    public ParkingPlaceNotBusyException(String message) {
        super(message);
    }
}

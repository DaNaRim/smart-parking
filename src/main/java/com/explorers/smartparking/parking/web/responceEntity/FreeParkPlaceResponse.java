package com.explorers.smartparking.parking.web.responceEntity;

public class FreeParkPlaceResponse {

    private Long number;

    private boolean isBusy;

    private boolean isBooked;

    public FreeParkPlaceResponse(Long number, boolean isBusy, boolean isBooked) {
        this.number = number;
        this.isBusy = isBusy;
        this.isBooked = isBooked;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}

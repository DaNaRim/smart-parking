package com.explorers.smartparking.parking.web.responceEntity;

public class FreeParkPlaceResponse {

    private Long number;

    private boolean isBusy;

    public FreeParkPlaceResponse(Long number, boolean isBusy) {
        this.number = number;
        this.isBusy = isBusy;
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
}

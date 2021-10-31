package com.explorers.smartparking.parking.web.responceEntity;

import java.util.List;

public class FreeParkResponse {

    private Long id;
    private Long x;
    private Long y;
    private List<FreeParkPlaceResponse> parkingPlaces;
    private Double pricePerHour;
    private Double distance;

    public FreeParkResponse(Long id,
                            Long x,
                            Long y,
                            List<FreeParkPlaceResponse> parkingPlaces,
                            Double pricePerHour,
                            Double distance) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.parkingPlaces = parkingPlaces;
        this.pricePerHour = pricePerHour;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public List<FreeParkPlaceResponse> getParkingPlaces() {
        return parkingPlaces;
    }

    public void setParkingPlaces(List<FreeParkPlaceResponse> parkingPlaces) {
        this.parkingPlaces = parkingPlaces;
    }

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}

package com.explorers.smartparking.parking.web.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ParkingDto {

    @NotNull
    private Long x;

    @NotNull
    private Long y;

    @NotNull
    @Min(1)
    private Integer countPlaces;

    @NotNull
    @Min(1)
    private Double pricePerHour;

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

    public Integer getCountPlaces() {
        return countPlaces;
    }

    public void setCountPlaces(Integer countPlaces) {
        this.countPlaces = countPlaces;
    }

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
}

package com.explorers.smartparking.parking.persistence.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long x;

    private Long y;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parking")
    private List<ParkingPlace> parkingPlaces;


    private Double pricePerHour;

    public Parking() {
    }

    public Parking(Long x, Long y, Double pricePerHour) {
        this.x = x;
        this.y = y;
        this.pricePerHour = pricePerHour;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ParkingPlace> getParkingPlaces() {
        return parkingPlaces;
    }

    public void setParkingPlaces(List<ParkingPlace> parkingPlaces) {
        this.parkingPlaces = parkingPlaces;
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

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Double price) {
        this.pricePerHour = price;
    }
}

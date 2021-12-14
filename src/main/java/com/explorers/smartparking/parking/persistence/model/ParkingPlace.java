package com.explorers.smartparking.parking.persistence.model;

import com.explorers.smartparking.user.persistence.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ParkingPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @JsonIgnore
    private Parking parking;

    @Column(nullable = false, updatable = false)
    private Long number;

    private Date dateOccupied;

    private Date dateBooked;

    @ManyToOne
    private User userOccupied;

    public ParkingPlace() {
    }

    public ParkingPlace(Long number, Parking parking) {
        this.number = number;
        this.parking = parking;
    }

    @JsonIgnore
    public boolean isBusy() {
        return dateOccupied != null;
    }

    @JsonIgnore
    public boolean isBooked() {
        return dateBooked != null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parking getParking() {
        return parking;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Date getDateOccupied() {
        return dateOccupied;
    }

    public void setDateOccupied(Date dateOccupied) {
        this.dateOccupied = dateOccupied;
    }

    public Date getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(Date dateBooked) {
        this.dateBooked = dateBooked;
    }

    public User getUserOccupied() {
        return userOccupied;
    }

    public void setUserOccupied(User userOccupied) {
        this.userOccupied = userOccupied;
    }
}

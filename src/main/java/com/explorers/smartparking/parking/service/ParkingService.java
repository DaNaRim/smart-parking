package com.explorers.smartparking.parking.service;

import com.explorers.smartparking.parking.persistence.model.Parking;
import com.explorers.smartparking.parking.web.dto.ParkingDto;
import com.explorers.smartparking.parking.web.responceEntity.FreeParkResponse;

import java.util.List;

public interface ParkingService {

    List<FreeParkResponse> getNearestFreeParkingSpaces(Long x, Long y);

    /**
     * @return current user balance
     */
    double occupyPlace(Long parkingId, Long placeNumber, String userEmail);

    /**
     * @return current user balance
     */
    double makeRoom(Long parkingId, Long placeNumber);

    void bookPlace(Long parkingId, Long placeNumber, String userEmail);

    Parking addParking(ParkingDto parkingDto);
}

package com.explorers.smartparking.parking.service;

import com.explorers.smartparking.parking.persistence.model.Parking;
import com.explorers.smartparking.parking.web.dto.ParkingDto;
import com.explorers.smartparking.parking.web.responceEntity.FreeParkResponse;
import java.util.List;

public interface ParkingService {

  List<FreeParkResponse> getNearestFreeParkingSpaces(long x, long y);

  /**
   * @return current user balance
   */
  double occupyPlace(long parkingId, long placeNumber, String userEmail);

  /**
   * @return current user balance
   */
  double makeRoom(long parkingId, long placeNumber);

  void bookPlace(long parkingId, long placeNumber, String userEmail);

  Parking addParking(ParkingDto parkingDto);
}

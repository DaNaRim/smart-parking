package com.explorers.smartparking.parking.web.controller;

import com.explorers.smartparking.parking.service.ParkingService;
import com.explorers.smartparking.parking.web.responceEntity.FreeParkResponse;
import com.explorers.smartparking.user.web.util.AuthorizationUtil;
import com.explorers.smartparking.user.web.util.GenericResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/park", "/{lang}/park"})
public class ParkingController {

  private final ParkingService parkingService;
  private final MessageSource messages;

  @Autowired
  public ParkingController(ParkingService parkingService, MessageSource messages) {
    this.parkingService = parkingService;
    this.messages = messages;
  }

  @GetMapping("/getFreeParkingPlaces")
  public List<FreeParkResponse> getFreeParkingPlaces(@RequestParam long x, @RequestParam long y) {

    return parkingService.getNearestFreeParkingSpaces(x, y);
  }

  @PutMapping("/bookPlace")
  public GenericResponse bookPlace(
      @RequestParam long parkingId, @RequestParam long placeNumber, HttpServletRequest request) {

    parkingService.bookPlace(parkingId, placeNumber, AuthorizationUtil.getUserEmail());
    return new GenericResponse(
        messages.getMessage("message.parking.booked", null, request.getLocale()));
  }
}

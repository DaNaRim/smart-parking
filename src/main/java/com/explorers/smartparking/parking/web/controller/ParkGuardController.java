package com.explorers.smartparking.parking.web.controller;

import com.explorers.smartparking.parking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/guard")
public class ParkGuardController {

    private final ParkingService parkingService;

    @Autowired
    public ParkGuardController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PutMapping("/occupyPlace")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void occupyPlace(@RequestParam Long parkingId,
                            @RequestParam Long placeNumber,
                            @RequestParam String userEmail) {

        parkingService.occupyPlace(parkingId, placeNumber, userEmail);
    }

    @PutMapping("/makeRoom")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void makeRoom(@RequestParam Long parkingId,
                         @RequestParam Long placeNumber) {

        parkingService.makeRoom(parkingId, placeNumber);
    }
}

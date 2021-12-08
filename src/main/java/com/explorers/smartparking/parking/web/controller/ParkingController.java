package com.explorers.smartparking.parking.web.controller;

import com.explorers.smartparking.parking.service.ParkingService;
import com.explorers.smartparking.parking.web.responceEntity.FreeParkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/park")
public class ParkingController {

    private final ParkingService parkingService;

    @Autowired
    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping("/getFreeParkingPlaces")
    public List<FreeParkResponse> getFreeParkingPlaces(@RequestParam Long x,
                                                       @RequestParam Long y) {

        return parkingService.getNearestFreeParkingSpaces(x, y);
    }

    @PutMapping("/occupyPlace")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void occupyPlace(@RequestParam Long parkingId,
                            @RequestParam Long placeNumber) {

        parkingService.occupyPlace(parkingId, placeNumber);
    }

    @PutMapping("/makeRoom")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void makeRoom(@RequestParam Long parkingId,
                         @RequestParam Long placeNumber) {

        parkingService.makeRoom(parkingId, placeNumber);
    }

    @PutMapping("/bookPlace")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void bookPlace(@RequestParam Long parkingId,
                          @RequestParam Long placeNumber) {

        parkingService.bookPlace(parkingId, placeNumber);
    }
}

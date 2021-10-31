package com.explorers.smartparking.parking.web.controller;

import com.explorers.smartparking.parking.persistence.model.Parking;
import com.explorers.smartparking.parking.persistence.model.ParkingPlace;
import com.explorers.smartparking.parking.service.ParkingService;
import com.explorers.smartparking.parking.web.dto.ParkingDto;
import com.explorers.smartparking.parking.web.responceEntity.FreeParkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ParkingPlace occupyPlace(@RequestParam Long parkingId,
                                    @RequestParam Long placeNumber) {
        return parkingService.occupyPlace(parkingId, placeNumber);
    }

    @PutMapping("/makeRoom")
    public ParkingPlace makeRoom(@RequestParam Long parkingId,
                                 @RequestParam Long placeNumber) {
        return parkingService.makeRoom(parkingId, placeNumber);
    }

    @PostMapping("/addPark")

    public Parking addParking(@Valid @RequestBody ParkingDto parkingDto) {
        return parkingService.addParking(parkingDto);
    }

}

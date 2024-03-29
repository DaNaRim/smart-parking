package com.explorers.smartparking.parking.web.controller;

import com.explorers.smartparking.parking.persistence.model.Parking;
import com.explorers.smartparking.parking.service.ParkingService;
import com.explorers.smartparking.parking.web.dto.ParkingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping({"/parkAdmin", "/{lang}/parkAdmin"})
public class ParkAdminController {

    private final ParkingService parkingService;

    @Autowired
    public ParkAdminController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/addPark")
    public Parking addParking(@Valid @RequestBody ParkingDto parkingDto) {
        return parkingService.addParking(parkingDto);
    }
}

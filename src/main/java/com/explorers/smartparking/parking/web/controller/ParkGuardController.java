package com.explorers.smartparking.parking.web.controller;

import com.explorers.smartparking.parking.service.ParkingService;
import com.explorers.smartparking.user.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping({"/guard", "/{lang}/guard"})
public class ParkGuardController {

    private final ParkingService parkingService;
    private final MessageSource messages;

    @Autowired
    public ParkGuardController(ParkingService parkingService, MessageSource messages) {
        this.parkingService = parkingService;
        this.messages = messages;
    }

    @PutMapping("/occupyPlace")
    public GenericResponse occupyPlace(@RequestParam long parkingId,
                                       @RequestParam long placeNumber,
                                       @RequestParam String userEmail,
                                       HttpServletRequest request) {

        parkingService.occupyPlace(parkingId, placeNumber, userEmail);
        return new GenericResponse(messages.getMessage("message.parking.occupied", null, request.getLocale()));
    }

    @PutMapping("/makeRoom")
    public GenericResponse makeRoom(@RequestParam long parkingId,
                                    @RequestParam long placeNumber,
                                    HttpServletRequest request) {

        parkingService.makeRoom(parkingId, placeNumber);
        return new GenericResponse(messages.getMessage("message.parking.dismissed", null, request.getLocale()));
    }
}

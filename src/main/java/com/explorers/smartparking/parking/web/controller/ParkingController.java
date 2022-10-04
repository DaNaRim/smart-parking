package com.explorers.smartparking.parking.web.controller;

import com.explorers.smartparking.parking.service.ParkingService;
import com.explorers.smartparking.parking.web.responceEntity.FreeParkResponse;
import com.explorers.smartparking.user.web.util.AuthorizationUtil;
import com.explorers.smartparking.user.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping({"/park", "/{lang}/park"})
public class ParkingController {

    private final ParkingService parkingService;
    private final MessageSource messages;

    @Autowired
    public ParkingController(ParkingService parkingService, MessageSource messages) {
        this.parkingService = parkingService;
        this.messages = messages;
    }

    @GetMapping("/parking")
    public String parking(Model model) {
        long x = Math.round(Math.random() * 100) - 50;
        long y = Math.round(Math.random() * 100) - 50;

        List<FreeParkResponse> parkingPlaces = parkingService.getNearestFreeParkingSpaces(x, y);
        model.addAttribute("parkingPlaces", parkingPlaces);
        model.addAttribute("coordinates", "x: " + x + ", y: " + y);

        return "parking";
    }

    @GetMapping("/getFreeParkingPlaces")
    public @ResponseBody
    List<FreeParkResponse> getFreeParkingPlaces(@RequestParam long x,
                                                @RequestParam long y) {

        return parkingService.getNearestFreeParkingSpaces(x, y);
    }

    @PutMapping("/bookPlace")
    public @ResponseBody
    GenericResponse bookPlace(@RequestParam long parkingId,
                              @RequestParam long placeNumber,
                              HttpServletRequest request) {

        parkingService.bookPlace(parkingId, placeNumber, AuthorizationUtil.getUserEmail());
        return new GenericResponse(messages.getMessage("message.parking.booked", null, request.getLocale()));
    }
}

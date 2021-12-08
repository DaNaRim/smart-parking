package com.explorers.smartparking.parking.service;

import com.explorers.smartparking.parking.exception.NoEnoughMoneyException;
import com.explorers.smartparking.parking.exception.ParkingExistsException;
import com.explorers.smartparking.parking.exception.ParkingPlaceBusyException;
import com.explorers.smartparking.parking.exception.ParkingPlaceNotFoundException;
import com.explorers.smartparking.parking.persistence.dao.ParkingDao;
import com.explorers.smartparking.parking.persistence.dao.ParkingPlaceDao;
import com.explorers.smartparking.parking.persistence.model.Parking;
import com.explorers.smartparking.parking.persistence.model.ParkingPlace;
import com.explorers.smartparking.parking.web.dto.ParkingDto;
import com.explorers.smartparking.parking.web.responceEntity.FreeParkPlaceResponse;
import com.explorers.smartparking.parking.web.responceEntity.FreeParkResponse;
import com.explorers.smartparking.user.persistence.model.User;
import com.explorers.smartparking.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ParkingServiceImpl implements ParkingService {

    private final ParkingDao parkingDao;
    private final ParkingPlaceDao parkingPlaceDao;
    private final UserService userService;

    @Autowired
    public ParkingServiceImpl(ParkingDao parkingDao, ParkingPlaceDao parkingPlaceDao, UserService userService) {
        this.parkingDao = parkingDao;
        this.parkingPlaceDao = parkingPlaceDao;
        this.userService = userService;
    }

    @Override
    public List<FreeParkResponse> getNearestFreeParkingSpaces(Long x, Long y) { //TODO change logic
        List<Parking> parkingList = parkingDao.getFreeParkingPlaces(x, y);

        List<FreeParkResponse> result = new ArrayList<>();

        for (Parking parking : parkingList) {

            List<FreeParkPlaceResponse> parkingPlaces = new ArrayList<>();
            for (ParkingPlace pp : parking.getParkingPlaces()) {
                parkingPlaces.add(new FreeParkPlaceResponse(pp.getNumber(), pp.isBusy(), pp.isBooked()));
            }
            result.add(new FreeParkResponse(
                    parking.getId(),
                    parking.getX(),
                    parking.getY(),
                    parkingPlaces,
                    parking.getPricePerHour(),
                    Math.round(Math.sqrt(
                            Math.pow(parking.getX() - x, 2) + Math.pow(parking.getY() - y, 2)) * 100.0) / 100.0 //count distance
            ));
        }
        return result;
    }

    @Override
    public double occupyPlace(Long parkingId, Long placeNumber, String userEmail) {
        ParkingPlace pp = getParkingPlace(parkingId, placeNumber);
        User user = userService.findByEmail(userEmail);

        if (pp.isBusy()) {
            throw new ParkingPlaceBusyException("Parking place already busy");

        } else if (pp.isBooked() && !pp.getUserOccupied().equals(user)) {
            throw new ParkingPlaceBusyException("Parking place booked by another user");

        } else if (user.getBalance() <= 0) {
            throw new NoEnoughMoneyException("No enough money on your balance: " + user.getBalance());

        } else if (pp.isBooked()) {
            double moneyToPay = pp.getParking().getPricePerHour() * getOccupiedHours(pp.getDateBooked());
            moneyToPay = Math.round(moneyToPay * 100.0) / 100.0;

            user.setBalance(user.getBalance() - moneyToPay);
        }
        pp.setDateOccupied(new Date());
        pp.setDateBooked(null);
        pp.setUserOccupied(user);
        return user.getBalance();
    }

    @Override
    public double makeRoom(Long parkingId, Long placeNumber) {
        ParkingPlace pp = getParkingPlace(parkingId, placeNumber);
        User user = userService.findByEmail(pp.getUserOccupied().getEmail());

        double moneyToPay = pp.getParking().getPricePerHour() * getOccupiedHours(pp.getDateOccupied());
        moneyToPay = Math.round(moneyToPay * 100.0) / 100.0;

        user.setBalance(user.getBalance() - moneyToPay);

        pp.setDateOccupied(null);
        pp.setUserOccupied(null);
        return user.getBalance();
    }

    @Override
    public void bookPlace(Long parkingId, Long placeNumber, String userEmail) {
        ParkingPlace pp = getParkingPlace(parkingId, placeNumber);
        User user = userService.findByEmail(userEmail);

        if (pp.isBooked() || pp.isBusy()) {
            throw new ParkingPlaceBusyException("Parking place is busy");

        } else if (user.getBalance() <= 0) {
            throw new NoEnoughMoneyException("No enough money on your balance: " + user.getBalance());
        }
        pp.setDateBooked(new Date());
        pp.setUserOccupied(user);
    }

    @Override
    public Parking addParking(ParkingDto parkingDto) {

        if (parkingDao.isExists(parkingDto.getX(), parkingDto.getY())) {
            throw new ParkingExistsException("There are parking in this place");
        }

        Parking parking = new Parking(
                parkingDto.getX(),
                parkingDto.getY(),
                parkingDto.getPricePerHour()
        );
        parkingDao.save(parking);

        List<ParkingPlace> parkingPlaces = new ArrayList<>();
        for (long i = 1; i < parkingDto.getCountPlaces() + 1; i++) {
            ParkingPlace pp = new ParkingPlace(i, parking);
            parkingPlaceDao.save(pp);
            parkingPlaces.add(pp);
        }
        parking.setParkingPlaces(parkingPlaces);
        return parking;
    }

    private ParkingPlace getParkingPlace(long parkingId, long placeNumber) {
        ParkingPlace pp = parkingPlaceDao.findByParkingAndNumber(parkingId, placeNumber);

        if (pp == null) {
            throw new ParkingPlaceNotFoundException("Wrong parking number");
        }
        return pp;
    }

    private double getOccupiedHours(Date date) {
        final int MILLI_TO_HOUR = 1000 * 60 * 60;
        return (double) (new Date().getTime() - date.getTime()) / MILLI_TO_HOUR;
    }

}

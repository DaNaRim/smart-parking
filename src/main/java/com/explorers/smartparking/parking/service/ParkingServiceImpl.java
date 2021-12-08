package com.explorers.smartparking.parking.service;

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

    @Autowired
    public ParkingServiceImpl(ParkingDao parkingDao, ParkingPlaceDao parkingPlaceDao) {
        this.parkingDao = parkingDao;
        this.parkingPlaceDao = parkingPlaceDao;
    }

    @Override
    public List<FreeParkResponse> getNearestFreeParkingSpaces(Long x, Long y) {
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
    public void occupyPlace(Long parkingId, Long placeNumber) {
        ParkingPlace pp = parkingPlaceDao.findByParkingAndNumber(parkingId, placeNumber);

        if (pp == null) {
            throw new ParkingPlaceNotFoundException("Wrong parking number");

        } else if (pp.getDateOccupied() != null) {
            throw new ParkingPlaceBusyException("Parking place already busy");
        }
        pp.setBusy(true);
        pp.setDateOccupied(new Date());
        pp.setBooked(false); //TODO money paid
        pp.setDateBooked(null);
    }

    @Override
    public void makeRoom(Long parkingId, Long placeNumber) {
        ParkingPlace pp = parkingPlaceDao.findByParkingAndNumber(parkingId, placeNumber);

        if (pp == null) {
            throw new ParkingPlaceNotFoundException("Wrong parking number");
        }
        pp.setBusy(false); //TODO money paid
        pp.setDateOccupied(null);
    }

    @Override
    public void bookPlace(Long parkingId, Long placeNumber) {
        ParkingPlace pp = parkingPlaceDao.findByParkingAndNumber(parkingId, placeNumber);

        if (pp == null) {
            throw new ParkingPlaceNotFoundException("Wrong parking number");

        } else if (pp.getDateBooked() != null || pp.getDateOccupied() != null) {
            throw new ParkingPlaceBusyException("Parking place already booked");
        }
        pp.setBooked(true);
        pp.setDateBooked(new Date());
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

}

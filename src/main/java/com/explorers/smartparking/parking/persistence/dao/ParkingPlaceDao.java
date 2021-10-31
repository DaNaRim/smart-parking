package com.explorers.smartparking.parking.persistence.dao;

import com.explorers.smartparking.parking.persistence.model.ParkingPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingPlaceDao extends JpaRepository<ParkingPlace, Long> {

    @Query(value = "SELECT * FROM parking_place" +
            " WHERE parking_id = :parkingId" +
            "       AND number = :number",
            nativeQuery = true)
    ParkingPlace findByParkingAndNumber(@Param(value = "parkingId") Long parking,
                                        @Param(value = "number") Long number);

}

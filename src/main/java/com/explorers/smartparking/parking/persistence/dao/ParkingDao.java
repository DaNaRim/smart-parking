package com.explorers.smartparking.parking.persistence.dao;

import com.explorers.smartparking.parking.persistence.model.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingDao extends JpaRepository<Parking, Long> {

    @Query(value = "SELECT EXISTS(" +
            "SELECT 1 " +
            "  FROM Parking " +
            " WHERE x = :x AND y = :y)",
            nativeQuery = true)
    boolean isExists(@Param("x") Long x, @Param("y") Long y);

    @Query(value = "SELECT p.*," +
            "       ARRAY_AGG(pp) AS parking_places," +
            "       ROUND(CAST(SQRT(POW(p.x - :x, 2) + POW(p.y - :y, 2)) AS NUMERIC), 2) AS distance" +
            "  FROM Parking AS p" +
            "       JOIN Parking_place AS pp ON p.id = pp.parking_id" +
            " WHERE pp.is_busy = FALSE" +
            " GROUP BY p.id" +
            " ORDER BY distance" +
            " LIMIT 20",
            nativeQuery = true)
    List<Parking> getFreeParkingPlaces(@Param("x") Long x, @Param("y") Long y);
}

package com.rdhxb.CityBike.bikeRepo;

import com.rdhxb.CityBike.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepo extends JpaRepository<Station, Long> {
    Station findByStationId(String stationId);
    List<Station> findAllByNumBikesAvailableGreaterThan(int numBikesAvailableIsGreaterThan);

    List<Station> findAllByNumDocksAvailableGreaterThan(int numDocksAvailableIsGreaterThan);
}

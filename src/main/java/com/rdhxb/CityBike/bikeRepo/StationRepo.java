package com.rdhxb.CityBike.bikeRepo;

import com.rdhxb.CityBike.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepo extends JpaRepository<Station, Long> {
}

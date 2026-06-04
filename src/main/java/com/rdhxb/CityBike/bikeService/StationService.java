package com.rdhxb.CityBike.bikeService;

import com.rdhxb.CityBike.bikeRepo.StationRepo;
import com.rdhxb.CityBike.entity.Station;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepo stationRepo;


    @Transactional
    public void saveAll(List<Station> stations){
        stationRepo.saveAll(stations);
    }


}

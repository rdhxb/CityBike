package com.rdhxb.CityBike.bikeService;

import com.rdhxb.CityBike.bikeRepo.StationRepo;
import com.rdhxb.CityBike.entity.Station;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepo stationRepo;

    public List<Station> getAll(){
        return stationRepo.findAll();
    }

    public Station getOneByStationId(String stationId){
        return stationRepo.findByStationId(stationId);
    }

    public List<Station> getWithDocksAvailable(){
        return stationRepo.findAllByNumBikesAvailableGreaterThan(0);
    }

    public List<Station> getWithBikesAvailable(){
        return stationRepo.findAllByNumDocksAvailableGreaterThan(0);
    }



    @Transactional
    public void saveAll(List<Station> stations){
        stationRepo.saveAll(stations);
    }


}

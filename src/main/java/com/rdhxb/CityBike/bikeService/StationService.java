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
        return stationRepo.findAllByNumDocksAvailableGreaterThan(0);
    }

    public List<Station> getWithBikesAvailable(){
        return stationRepo.findAllByNumBikesAvailableGreaterThan(0);
    }



    @Transactional
    public void saveAll(List<Station> stations){
        stationRepo.saveAll(stations);
    }

    @Transactional
    public void deleteAll(){
        stationRepo.deleteAll();
    }

// method that will take incoming merged data and will add not existing and override existing Objects with new data
    @Transactional
    public void updateIncomingData(List<Station> stations){
        for (Station s : stations){
            String incomingStationId = s.getStationId();
            if (stationRepo.findByStationId(incomingStationId) == null){
                stationRepo.save(s);
            }else{
                Station updateStation = stationRepo.findByStationId(incomingStationId);
                updateStation.setName(s.getName());
                updateStation.setLon(s.getLon());
                updateStation.setLat(s.getLat());
                updateStation.setCapacity(s.getCapacity());
                updateStation.setRenting(s.isRenting());
                updateStation.setReturning(s.isReturning());
                updateStation.setNumDocksAvailable(s.getNumDocksAvailable());
                updateStation.setNumBikesAvailable(s.getNumBikesAvailable());
            }

        }
    }


}

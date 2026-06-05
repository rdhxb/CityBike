package com.rdhxb.CityBike.controller;

import com.rdhxb.CityBike.bikeService.StationService;
import com.rdhxb.CityBike.entity.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class BikeStationController {
    private final StationService stationService;

    @GetMapping()
    public List<Station> getAll(){
        return stationService.getAll();
    }

    @GetMapping("/{stationId}")
    public Station getStationByStationId(@PathVariable String stationId){
        return stationService.getOneByStationId(stationId);
    }

    @GetMapping("/docks")
    public List<Station> getWithDocks(){
        return stationService.getWithDocksAvailable();
    }

    @GetMapping("/bikes")
    public List<Station> getWithBikes(){
        return stationService.getWithBikesAvailable();
    }


}

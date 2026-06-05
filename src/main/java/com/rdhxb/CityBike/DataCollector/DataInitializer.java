package com.rdhxb.CityBike.DataCollector;

import com.rdhxb.CityBike.bikeService.StationService;
import com.rdhxb.CityBike.entity.Station;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

    private final StationService stationService;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();

    //    method that collect data from 2 APIs and Merge them into Object (Station) by using "mergeData" method
    @Scheduled(fixedRate = 60_000)
    public void collectMergeSave() throws IOException, InterruptedException {
        try {


//        Create request for first endpoint
            HttpRequest requestForStationInfo = HttpRequest.newBuilder()
                    .uri(URI.create("https://gbfs.nextbike.net/maps/gbfs/v2/nextbike_vw/pl/station_information.json"))
                    .GET()
                    .build();

            HttpResponse<String> responseStationInfo = client.send(requestForStationInfo, HttpResponse.BodyHandlers.ofString());

//        Using mapper create root from first request
            JsonNode rootStationInfo = mapper.readTree(responseStationInfo.body());

//        get all station data []
            JsonNode stationsData = rootStationInfo.get("data").get("stations");

//        Lists to hold Objects from both APIs
            List<StationInfo> stationInfoList = new ArrayList<>();
            List<StationStatus> stationStatusList = new ArrayList<>();

            //        Loop to get that many elements that we want from first API
            for (int i = 0; i < stationsData.size(); i++) {

//            Getting need info
                    String stationId = stationsData.get(i).path("station_id").asString();
                    String stationName = stationsData.get(i).path("name").asString();
                    Double stationLat = stationsData.get(i).path("lat").asDouble(0.0);
                    Double stationLon = stationsData.get(i).path("lon").asDouble(0.0);
                    int stationCapacity = stationsData.get(i).path("capacity").asInt();

//            Create Object from data
                    StationInfo station = new StationInfo(
                            stationId,
                            stationName,
                            stationLat,
                            stationLon,
                            stationCapacity
                    );
//            saving object to our Array
                    stationInfoList.add(station);
                }


//        Create sec request
            HttpRequest requestForStationStatus = HttpRequest.newBuilder(URI.create("https://gbfs.nextbike.net/maps/gbfs/v2/nextbike_vw/pl/station_status.json"))
                    .GET()
                    .build();

            HttpResponse<String> responseStationStatus = client.send(requestForStationStatus, HttpResponse.BodyHandlers.ofString());


//        create root from sec response
            JsonNode rootStatusStationsData = mapper.readTree(responseStationStatus.body());

            JsonNode statusRoot = rootStatusStationsData.get("data").get("stations");


//        Loop to get that many elements that we want from  second API
            for (int i = 0; i < statusRoot.size(); i++) {

//            Get needed information
                    String statusStationId = statusRoot.get(i).path("station_id").asString();
                    int numBikesAvailable = statusRoot.get(i).path("num_bikes_available").asInt();
                    int numDocksAvailable = statusRoot.get(i).path("num_docks_available").asInt();
                    boolean isRenting = statusRoot.get(i).path("is_renting").asBoolean();
                    boolean isReturning = statusRoot.get(i).path("is_returning").asBoolean();

//            Create Object from data we collect
                    StationStatus stationStatus = new StationStatus(
                            statusStationId,
                            numBikesAvailable,
                            numDocksAvailable,
                            isRenting,
                            isReturning
                    );
//            Save Object from sec API to List
                    stationStatusList.add(stationStatus);
                }


//        Create List for merged Objects
            List<Station> mergedDataList = mergeData(stationInfoList, stationStatusList);

//            Use updateIncomingData method to load new Data
            stationService.updateIncomingData(mergedDataList);
            log.info("Schedule refresh works add: " + mergedDataList.size() + " records to db");
        } catch (Exception e) {
            log.warn("Shedule refresh does not work skipping !");
        }

    }

    public static List<Station> mergeData(List<StationInfo> stations, List<StationStatus> stationStatusList) {

//        Create list for merged Object
        List<Station> mergedStations = new ArrayList<>();
        Map<String, StationInfo> stationInfoMap = new HashMap<>();
        Map<String, StationStatus> stationStatusMap = new HashMap<>();

        for (StationInfo s : stations){
            stationInfoMap.put(s.getId(),s);
        }
        for (StationStatus s : stationStatusList){
            stationStatusMap.put(s.getId(), s);
        }

        for (StationInfo info : stationInfoMap.values()) {
            StationStatus status = stationStatusMap.get(info.getId());
            if (status == null) {
                continue;
            }
            mergedStations.add(new Station(
                    null,
                    info.getId(),
                    info.getName(),
                    info.getLat(),
                    info.getLon(),
                    info.getCapacity(),
                    status.getNumBikesAvailable(),
                    status.getNumDocksAvailable(),
                    status.isRenting(),
                    status.isReturning()
            ));
        }
        return mergedStations;

    }

}

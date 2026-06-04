package com.rdhxb.CityBike.DataCollector;

import com.rdhxb.CityBike.bikeService.StationService;
import com.rdhxb.CityBike.entity.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final  StationService stationService;
    private final int limit = 3;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();

    public List<Station> collectMergeSave() throws IOException, InterruptedException {

        HttpRequest requestForStationInfo = HttpRequest.newBuilder()
                .uri(URI.create("https://gbfs.nextbike.net/maps/gbfs/v2/nextbike_vw/pl/station_information.json"))
                .GET()
                .build();

        HttpResponse<String> responseStationInfo = client.send(requestForStationInfo,HttpResponse.BodyHandlers.ofString());

        JsonNode rootStationInfo = mapper.readTree(responseStationInfo.body());

        //        get all station data []
        JsonNode stationsData = rootStationInfo.get("data").get("stations");

        List<StationInfo> stationInfoList = new ArrayList<>();
        List<StationStatus> stationStatusList = new ArrayList<>();

        for (int i = 0; i < limit; i++) {

            String stationId = stationsData.get(i).path("station_id").asString();
            String stationName = stationsData.get(i).path("name").asString();
            Double stationLat = stationsData.get(i).path("lat").asDouble(0.0);
            Double stationLon = stationsData.get(i).path("lon").asDouble(0.0);
            int stationCapacity = stationsData.get(i).path("capacity").asInt();

            StationInfo station = new StationInfo(
                    stationId,
                    stationName,
                    stationLat,
                    stationLon,
                    stationCapacity
            );
            stationInfoList.add(station);
        }


        HttpRequest requestForStationStatus = HttpRequest.newBuilder(URI.create("https://gbfs.nextbike.net/maps/gbfs/v2/nextbike_vw/pl/station_status.json"))
                .GET()
                .build();

        HttpResponse<String> responseStationStatus = client.send(requestForStationStatus, HttpResponse.BodyHandlers.ofString());


        JsonNode statusStationsData = mapper.readTree(responseStationStatus.body());

        JsonNode statusRoot = statusStationsData.get("data").get("stations");

        for (int i = 0; i < limit; i++) {


            String statusStationId = statusRoot.get(i).path("station_id").asString();
            int numBikesAvailable = statusRoot.get(i).path("num_bikes_available").asInt();
            int numDocksAvailable = statusRoot.get(i).path("num_docks_available").asInt();
            boolean isRenting = statusRoot.get(i).path("is_renting").asBoolean();
            boolean isReturning = statusRoot.get(i).path("is_returning").asBoolean();

            StationStatus stationStatus = new StationStatus(
                    statusStationId,
                    numBikesAvailable,
                    numDocksAvailable,
                    isRenting,
                    isReturning
            );

            stationStatusList.add(stationStatus);
        }

        List<Station> mergedDataList = mergeData(stationInfoList,stationStatusList);

        System.out.println(mergedDataList);
        stationService.saveAll(mergedDataList);

        return mergedDataList;
    }

    public static List<Station> mergeData(List<StationInfo> stations, List<StationStatus> stationStatusList){

        List<Station> mergedStations = new ArrayList<>();

        for (int i = 0; i < stations.size(); i++) {

            Station station = new Station(
                    null,
                    stations.get(i).getId(),
                    stations.get(i).getName(),
                    stations.get(i).getLat(),
                    stations.get(i).getLon(),
                    stations.get(i).getCapacity(),
                    stationStatusList.get(i).getNumBikesAvailable(),
                    stationStatusList.get(i).getNumDocksAvailable(),
                    stationStatusList.get(i).isRenting(),
                    stationStatusList.get(i).isReturning()
            );
            mergedStations.add(station);

        }
        return mergedStations;

    }

}

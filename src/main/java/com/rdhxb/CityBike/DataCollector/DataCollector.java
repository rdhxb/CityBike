package com.rdhxb.CityBike.DataCollector;


import com.rdhxb.CityBike.entity.Station;
import com.rdhxb.CityBike.entity.StationStatus;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class DataCollector {
//    https://gbfs.nextbike.net/maps/gbfs/v2/nextbike_vw/pl/station_information.json
//    https://gbfs.nextbike.net/maps/gbfs/v2/nextbike_vw/pl/station_status.json
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest requestForStationInfo = HttpRequest.newBuilder()
                .uri(URI.create("https://gbfs.nextbike.net/maps/gbfs/v2/nextbike_vw/pl/station_information.json"))
                .GET()
                .build();

            HttpResponse<String> responseStationInfo = client.send(requestForStationInfo,HttpResponse.BodyHandlers.ofString());
//            System.out.println(responseStationInfo.statusCode());
//            System.out.println(responseStationInfo.body());


        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootStationInfo = mapper.readTree(responseStationInfo.body());

//        System.out.println(rootStationInfo);


//        get all station data []
        JsonNode stationsData = rootStationInfo.get("data").get("stations");

        for (int i = 0; i < 5; i++) {

//        get first station id
            String stationId = stationsData.get(i).path("station_id").asString();
            String stationName = stationsData.get(i).path("name").asString();
            Double stationLat = stationsData.get(i).path("lat").asDouble(0.0);
            Double stationLon = stationsData.get(i).path("lon").asDouble(0.0);
            int stationCapacity = stationsData.get(i).path("capacity").asInt();

            System.out.println(stationLat);

            Station s1 = new Station(
                    stationId,
                    stationName,
                    stationLat,
                    stationLon,
                    stationCapacity
            );
            System.out.println(s1);

        }




        HttpRequest requestForStationStatus = HttpRequest.newBuilder(URI.create("https://gbfs.nextbike.net/maps/gbfs/v2/nextbike_vw/pl/station_status.json"))
                .GET()
                .build();

        HttpResponse<String> responseStationStatus = client.send(requestForStationStatus, HttpResponse.BodyHandlers.ofString());

        System.out.println(responseStationStatus.statusCode());

        JsonNode statusStationsData = mapper.readTree(responseStationStatus.body());

        JsonNode statusRoot = statusStationsData.get("data").get("stations");

        String statusStationId = statusRoot.get(0).path("station_id").asString();
        int numBikesAvailable = statusRoot.get(0).path("num_bikes_available").asInt();
        int numDocksAvailable = statusRoot.get(0).path("num_docks_available").asInt();
        boolean isRenting = statusRoot.get(0).path("is_renting").asBoolean();
        boolean isReturning = statusRoot.get(0).path("is_returning").asBoolean();

        StationStatus stationStatus = new StationStatus(
                statusStationId,
                numBikesAvailable,
                numDocksAvailable,
                isRenting,
                isReturning
        );
        System.out.println(stationStatus);




    }


}

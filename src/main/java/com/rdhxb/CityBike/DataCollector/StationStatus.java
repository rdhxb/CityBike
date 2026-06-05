package com.rdhxb.CityBike.DataCollector;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationStatus {

    private String id;

    private int numBikesAvailable;
    private int numDocksAvailable;
    private boolean isRenting;
    private boolean isReturning;

}

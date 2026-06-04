package com.rdhxb.CityBike.DataCollector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationInfo {
    private String id;

    private String name;
    private Double lat;
    private Double lon;

    private int capacity;
}

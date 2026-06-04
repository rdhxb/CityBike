package com.rdhxb.CityBike.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationStatus {

    @Id
    private String id;

    private int numBikesAvailable;
    private int numDocksAvailable;
    private boolean isRenting;
    private boolean isReturning;

}

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
public class Station {
    @Id
    private String id;

    private String name;
    private Double lat;
    private Double lon;

    private int capacity;
}

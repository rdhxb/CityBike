package com.rdhxb.CityBike.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stations")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String stationId;
    private String name;
    private Double lat;
    private Double lon;
    private int capacity;

    private int numBikesAvailable;
    private int numDocksAvailable;
    private boolean isRenting;
    private boolean isReturning;

}

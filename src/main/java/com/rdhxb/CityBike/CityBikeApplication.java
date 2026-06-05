package com.rdhxb.CityBike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class CityBikeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityBikeApplication.class, args);
	}

}

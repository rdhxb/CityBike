package com.rdhxb.CityBike;

import com.rdhxb.CityBike.DataCollector.DataInitializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class CityBikeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityBikeApplication.class, args);
	}

	@Bean
	CommandLineRunner InitData(DataInitializer dataInitializer){
		return args -> dataInitializer.collectMergeSave();
	}
}

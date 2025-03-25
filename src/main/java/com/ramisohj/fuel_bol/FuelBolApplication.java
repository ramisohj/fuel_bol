package com.ramisohj.fuel_bol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;


@SpringBootApplication
@EnableScheduling
public class FuelBolApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("Etc/GMT+4"));
		SpringApplication.run(FuelBolApplication.class, args);

	}

}

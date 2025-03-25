package com.ramisohj.fuel_bol;

import com.ramisohj.fuel_bol.model.FuelMonitoring;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class FuelBolApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(FuelBolApplication.class, args);
		context.getBean(FuelMonitoring.class);
	}

}

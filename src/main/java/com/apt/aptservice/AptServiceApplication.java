package com.apt.aptservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AptServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AptServiceApplication.class, args);
	}

	@Bean
	public ApartmentMaintenanceCalculator homeService() {
		return new ApartmentMaintenanceCalculator();
	}

	@Override
	public void run(String... args) throws Exception {
		homeService().calculate();
	}

}

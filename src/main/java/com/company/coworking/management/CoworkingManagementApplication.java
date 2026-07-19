package com.company.coworking.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CoworkingManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoworkingManagementApplication.class, args);
	}

}

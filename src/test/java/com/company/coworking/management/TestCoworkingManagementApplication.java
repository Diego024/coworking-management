package com.company.coworking.management;

import org.springframework.boot.SpringApplication;

public class TestCoworkingManagementApplication {

	public static void main(String[] args) {
		SpringApplication.from(CoworkingManagementApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

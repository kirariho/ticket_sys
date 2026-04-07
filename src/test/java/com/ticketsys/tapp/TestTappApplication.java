package com.ticketsys.tapp;

import org.springframework.boot.SpringApplication;

public class TestTappApplication {

	public static void main(String[] args) {
		SpringApplication.from(TappApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

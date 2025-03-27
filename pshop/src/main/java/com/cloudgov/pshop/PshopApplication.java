package com.cloudgov.pshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
//@EnableReactiveMongoRepositories
public class PshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(PshopApplication.class, args);
	}

}

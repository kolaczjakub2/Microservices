package com.kolaczjakub.photoapp.discovery.PhotoAppDiscoverService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class PhotoAppDiscoverServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoAppDiscoverServiceApplication.class, args);
	}

}

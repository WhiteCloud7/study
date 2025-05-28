package com.whitecloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class gateWayApplication {

	public static void main(String[] args) {
		SpringApplication.run(gateWayApplication.class, args);
	}

}

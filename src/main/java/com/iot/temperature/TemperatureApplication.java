package com.iot.temperature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TemperatureApplication extends org.springframework.boot.web.servlet.support.SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication.run(TemperatureApplication.class, args);
	}

}


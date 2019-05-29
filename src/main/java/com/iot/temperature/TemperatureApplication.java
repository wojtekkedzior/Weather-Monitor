package com.iot.temperature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

@SpringBootApplication
@EnableMBeanExport(registration=RegistrationPolicy.IGNORE_EXISTING)
@EnableCaching
public class TemperatureApplication extends org.springframework.boot.web.servlet.support.SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication.run(TemperatureApplication.class, args);
	}

}


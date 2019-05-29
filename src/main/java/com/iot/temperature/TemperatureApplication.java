package com.iot.temperature;

import java.util.List;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

import com.iot.temperature.model.Temperature;

@SpringBootApplication
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@EnableCaching
public class TemperatureApplication extends org.springframework.boot.web.servlet.support.SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TemperatureApplication.class, args);
	}

	 @Bean
	 public Factory<SimpleCacheEntryListener> getListenerFactory() {
		 return FactoryBuilder.factoryOf(new SimpleCacheEntryListener());
	 }

	@Bean
	public Cache<Integer, List<Temperature>> getCache(CacheManager cacheManager) {
		MutableConfiguration<Integer, List<Temperature>> config = new MutableConfiguration<>();
		config.setExpiryPolicyFactory(new FactoryBuilder.SingletonFactory<>(new TemperatureExpiryPolicy()));
		
		Cache<Integer, List<Temperature>> cache = cacheManager.createCache("duration", config);
		
		MutableCacheEntryListenerConfiguration<Integer, List<Temperature>> listenerConfiguration = 
				new MutableCacheEntryListenerConfiguration<Integer, List<Temperature>>(getListenerFactory(), null, false, true);
		
		cache.registerCacheEntryListener(listenerConfiguration);
		return cache;
	}

}

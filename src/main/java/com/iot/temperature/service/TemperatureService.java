package com.iot.temperature.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.iot.temperature.model.Temperature;
import com.iot.temperature.repository.TemperatureRepository;

@Service
public class TemperatureService {

	@Autowired
	private TemperatureRepository temperatureRepo;

	@Cacheable("duration")
	public List<Temperature> getTemperatures(int duration) {
		Iterable<Temperature> findAll = temperatureRepo.findLastTemperatures(duration);
		return StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());
	}

}

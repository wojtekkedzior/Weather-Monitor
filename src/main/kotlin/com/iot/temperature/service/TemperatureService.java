package com.iot.temperature.service;

import com.iot.temperature.model.Temperature;
import com.iot.temperature.repository.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//This class cannot be converted to Kotlin as the Cachable annotations prevent the compiler sub creating a sub class for some reason
@Service
public class TemperatureService {

    @Autowired
    private TemperatureRepository temperatureRepo;

    @Cacheable("duration")
    public List<Temperature> getTemperatures(int duration) {
        Iterable<Temperature> findAll = temperatureRepo.findLastTemperatures(duration);
        return StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());
    }

    //    @Cacheable("duration")
    public List<Temperature> getTemperaturesForDay(LocalDateTime date) {
        Iterable<Temperature> findAll = temperatureRepo.findLastTemperaturesByTimestamp(date);
        return StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());
    }

    //        @Cacheable("duration")
    public List<Temperature> getTemperatures(LocalDateTime start, LocalDateTime end) {
        Iterable<Temperature> findAll = temperatureRepo.findTemperaturesByTimestampBetween(start, end);
        return StreamSupport.stream(findAll.spliterator(), true).collect(Collectors.toList());
    }


}

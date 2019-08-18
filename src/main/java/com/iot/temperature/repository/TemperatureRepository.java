package com.iot.temperature.repository;

import com.iot.temperature.model.Temperature;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

    @Query(value = "SELECT * FROM iot.temperature ORDER BY id DESC LIMIT ?1 ", nativeQuery = true)
    List<Temperature> findLastTemperatures(int duration);

    List<Temperature> findLastTemperaturesByTimestamp(LocalDateTime date);

    List<Temperature> findTemperaturesByTimestampBetween(LocalDateTime start, LocalDateTime end);
}

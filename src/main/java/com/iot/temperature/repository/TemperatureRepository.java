package com.iot.temperature.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iot.temperature.model.Temperature;

public interface TemperatureRepository extends CrudRepository<Temperature, Long>{
	
 @Query(value = "SELECT * FROM iot.temperature ORDER BY id DESC LIMIT ?1 ", nativeQuery = true)
	List<Temperature> findLastTemperatures(int size) ;  
}
//86400
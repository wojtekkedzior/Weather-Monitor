package com.iot.temperature.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.iot.temperature.model.Temperature;
import com.iot.temperature.repository.TemperatureRepository;

@RestController
public class TemperatureController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TemperatureRepository temperatureRepo;

	@PostMapping("/temperature/{temperature}/humidity/{humidity}/pressure/{pressure}")
	public ModelAndView createPayment(@PathVariable Optional<Float> temperature, @PathVariable Optional<Float> humidity, @PathVariable Optional<Float> pressure) {

//		 log.info("temperature: " + temperature);
//		 log.info("humidity: " + humidity);
//		 log.info("pressure: " + pressure);

		Temperature temp = new Temperature();
		temp.setTemperature(temperature.get());
		temp.setHumidity(humidity.get());
		temp.setPressure(pressure.get());
		temp.setTimestamp(new Date());
		temperatureRepo.save(temp);

		return new ModelAndView("redirect:/checkoutReservation/");
	}

	@GetMapping(path = "/temperature", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> sayHello() {

		List<Map<String, Object>> allValues = new ArrayList<>();
		Iterable<Temperature> findAll = temperatureRepo.findLastTemperatures();
		List<Temperature> collect = StreamSupport.stream(findAll.spliterator(), false).collect(Collectors.toList());

		for (Temperature temperature : collect) {
			HashMap<String, Object> map = new HashMap<>();

			map.put("temperature", temperature.getTemperature());
			map.put("time", temperature.getTimestamp().getTime());
			map.put("humidity", temperature.getHumidity());
			map.put("pressure", temperature.getPressure());
			allValues.add(map);
		}

		return new ResponseEntity<Object>(allValues, HttpStatus.OK);
	}
}

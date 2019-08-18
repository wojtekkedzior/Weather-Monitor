package com.iot.temperature.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.*;

import com.iot.temperature.model.TempPerHour;
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
import com.iot.temperature.service.TemperatureService;

@RestController
public class TemperatureController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TemperatureRepository temperatureRepo;
	
	@Autowired
	private TemperatureService service;

	@PostMapping("/temperature/{temperature}/humidity/{humidity}/pressure/{pressure}")
	public ModelAndView createPayment(@PathVariable Optional<Float> temperature, @PathVariable Optional<Float> humidity, @PathVariable Optional<Float> pressure) {

//		 log.info("temperature: " + temperature);
//		 log.info("humidity: " + humidity);
//		 log.info("pressure: " + pressure);

		Temperature temp = new Temperature();
		temp.setTemperature(temperature.get());
		temp.setHumidity(humidity.get());
		temp.setPressure(pressure.get());
		temp.setTimestamp(LocalDateTime.now());
		temperatureRepo.save(temp);

		return new ModelAndView("redirect:/checkoutReservation/");
	}

	@GetMapping(path = "/temperature/{duration}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getTemperatureData(@PathVariable Integer duration) {
		List<Map<String, Number>> allValues = new ArrayList<>();
		Map<LocalDateTime, Double> allAverages = new HashMap<>();

		LocalDateTime now = LocalDateTime.now();
		now = now.withNano(0).withHour(0);

		if(duration == 1) {

			for (int i = 0; i < 30; i++) {
				List<Temperature> collect = service.getTemperaturesForDay(now);

				//TODO this needs to be broken down per hour
				OptionalDouble average = collect.stream().mapToDouble(c -> c.getTemperature()).average();

				if(average.isEmpty()) {
					allAverages.put(now, 0d);
					System.out.println(0);
				} else {
					allAverages.put(now, average.getAsDouble());
					System.out.println(average.getAsDouble());
				}

				now = now.minus(1, ChronoUnit.DAYS);
			}

			System.out.println(allAverages);

//			IntSummaryStatistics stats = new IntSummaryStatistics();
//			stats.accept(1);
//			stats.accept(3);
//			stats.getAverage();

		} else {
			List<Temperature> collect = service.getTemperatures(duration);

			for (Temperature temperature : collect) {
				HashMap<String, Number> map = new HashMap<>();

				map.put("temperature", temperature.getTemperature());
				map.put("time", Date.from( temperature.getTimestamp().atZone( ZoneId.systemDefault()).toInstant()).getTime());
				map.put("humidity", temperature.getHumidity());
				map.put("pressure", temperature.getPressure());
				allValues.add(map);
			}
		}

		return new ResponseEntity<Object>(allValues, HttpStatus.OK);
	}

	@GetMapping(path = "/temperature/runningAverage", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getTemperatureData() throws InterruptedException {
		ConcurrentSkipListMap<LocalDateTime, Number> hourlyAverages = new ConcurrentSkipListMap<>();
		ExecutorService executor = Executors.newFixedThreadPool(100);
		LocalDateTime today = LocalDateTime.now().withNano(0).withSecond(0).withMinute(0).withHour(0);
		List<Map<String, Number>> allValues = new ArrayList<>();

		for (int i = 0; i < 30; i++) {
			for(int j = 0; j < 23; j++) {
				LocalDateTime finalToday = today;
				executor.execute(() -> doStuff(finalToday, hourlyAverages));
				today = today.plus(1, ChronoUnit.HOURS);
			}

			today = today.minus(1, ChronoUnit.DAYS).withHour(0);;
		}

		while(hourlyAverages.size() != 690) {
			Thread.sleep(100);
		}

		today = LocalDateTime.now().plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.HOURS).withNano(0).withSecond(0).withMinute(0);

		for (int k = 0; k < 30; k++) {
			for(int i = 0; i < 6; i++) {
				List<Number> averageForFourHours = new ArrayList<>();

				for(int j = 0; j < 4; j++) {
					averageForFourHours.add(hourlyAverages.getOrDefault(today, 0));
					today = today.minus(1, ChronoUnit.HOURS);
				}

//				System.out.println(today.minus(4, ChronoUnit.HOURS) + " " + averageForFourHours.stream().mapToDouble(n -> n.doubleValue()).average());
				Map<String, Number> range = new HashMap<>();
				range.put("temperature", averageForFourHours.stream().mapToDouble(n -> n.doubleValue()).average().getAsDouble());
				range.put("time", Date.from(today.minus(4, ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant()).getTime());
				System.err.println(averageForFourHours.stream().mapToDouble(n -> n.doubleValue()).average().getAsDouble() + " " + today.minus(4, ChronoUnit.HOURS));
				allValues.add(range);

			}

			today = today.minus(1, ChronoUnit.DAYS).withHour(0);
		}
		return new ResponseEntity<Object>(allValues, HttpStatus.OK);
	}

	private void doStuff(LocalDateTime today, ConcurrentSkipListMap<LocalDateTime, Number> hourlyAverages) {
//		System.out.println("Looking for: " + today + " and : " + today.plus(1, ChronoUnit.HOURS)) ;
		List<Temperature> collect = service.getTemperatures(today, today.plus(1, ChronoUnit.HOURS));
		System.out.println("Looking for: " + today + " and : " + today.plus(1, ChronoUnit.HOURS) + " found: " + collect.size());

		OptionalDouble average = collect.parallelStream().mapToDouble(c -> c.getTemperature()).average();
		hourlyAverages.put(today, average.isEmpty() ? 0 : average.getAsDouble());
	}
}
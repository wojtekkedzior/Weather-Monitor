package com.iot.temperature.controllers

import com.iot.temperature.model.Temperature
import com.iot.temperature.repository.TemperatureRepository
import com.iot.temperature.service.TemperatureService
import org.jetbrains.annotations.NotNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.Executors
import javax.cache.Cache

@RestController
class TemperatureController {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var temperatureRepo: TemperatureRepository

    @Autowired
    lateinit var service: TemperatureService

    @Autowired
    var cache: Cache<Int, ConcurrentSkipListMap<LocalDateTime, Number>>? = null

    val latestRecord: ResponseEntity<Temperature?>
        @GetMapping(path = ["/temperature/getLatest"], produces = [MediaType.APPLICATION_JSON_VALUE])
        get() {
            val topByTimestampDesc = temperatureRepo!!.findTopByOrderByIdDesc()
            log.info("Latest record: $topByTimestampDesc.timestamp.format(DateTimeFormatter.ISO_DATE_TIME))")
            return ResponseEntity(topByTimestampDesc, HttpStatus.OK)
        }

    @GetMapping(path = ["/temperature/runningAverage/{totalDays}/{hourWindow}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun runningAverage(@PathVariable totalDays: Int?, @PathVariable @NotNull hourWindow: Int): ResponseEntity<Any> {
        val allValues = ArrayList<Map<String, Number>>()
        val today = LocalDateTime.now().withNano(0).withSecond(0).withMinute(0).withHour(0)
        val totalHours: Int = totalDays?.times(24) ?: 0
        val hourlyAverages = concurrentSkipListMap(hourWindow, totalHours, today)

        for (i in 0..totalHours.div(hourWindow)) {
            val averageForFourHours = ArrayList<Number>()

            for (j in 1..hourWindow) {
                averageForFourHours.add((hourlyAverages).getOrDefault(today.minusHours(i.times(hourWindow).plus(j).toLong()), 0))
            }
            val range = HashMap<String, Number>()

            range["temperature"] = averageForFourHours.stream().mapToDouble { n -> n.toDouble() }.average().asDouble
            range["time"] = Date.from(today.minusHours((i * hourWindow).toLong()).atZone(ZoneId.systemDefault()).toInstant()).time
            allValues.add(range)
        }
        return ResponseEntity(allValues, HttpStatus.OK)
    }

    private fun concurrentSkipListMap(cacheKey: Int, totalHours: Int, today: LocalDateTime): ConcurrentSkipListMap<LocalDateTime, Number> {
        val executor = Executors.newFixedThreadPool(10)
        var hourlyAverages = ConcurrentSkipListMap<LocalDateTime, Number>()

        if (cache!!.containsKey(cacheKey)) {
            log.info("Found entry in cache")
            hourlyAverages = cache!!.get(cacheKey)
        } else {
            for (i in 0..totalHours) {
                executor.execute { doStuff(today.minus(i.toLong(), ChronoUnit.HOURS), hourlyAverages) }
            }

            while (hourlyAverages.size != totalHours) Thread.sleep(10)
            cache!!.put(cacheKey, hourlyAverages)
        }
        return hourlyAverages
    }


    @PostMapping("/temperature/{temperature}/humidity/{humidity}/pressure/{pressure}")
    fun createPayment(@PathVariable temperature: Optional<Double>, @PathVariable humidity: Optional<Float>, @PathVariable pressure: Optional<Float>): ModelAndView {
        val temp = Temperature()
        temp.temperature = temperature.get()
        temp.humidity = humidity.get()
        temp.pressure = pressure.get()
        temp.timestamp = LocalDateTime.now()
        temperatureRepo!!.save(temp)

        return ModelAndView("redirect:/checkoutReservation/")
    }

    @GetMapping(path = ["/temperature/{duration}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getTemperatureData(@PathVariable duration: Int?): ResponseEntity<Any> {
        val allValues = ArrayList<Map<String, Number>>()
        val allAverages = HashMap<LocalDateTime, Double>()

        var now = LocalDateTime.now().withNano(0).withHour(0)

        if (duration == 1) {
            for (i in 0..29) {
                val collect = service!!.getTemperaturesForDay(now)

                //TODO this needs to be broken down per hour
                val average = collect.stream().mapToDouble { c -> c!!.temperature!!.toDouble() }.average()

                if (average.isEmpty) {
                    allAverages[now] = 0.0
                } else {
                    allAverages[now] = average.asDouble
                }

                now = now.minus(1, ChronoUnit.DAYS)
            }
        } else {
            for (temperature in service!!.getTemperatures(duration!!)) {
                val map = HashMap<String, Number>()

                map["temperature"] = temperature.temperature!!.toDouble()
                map["time"] = Date.from(temperature.timestamp!!.atZone(ZoneId.systemDefault()).toInstant()).time
                map["humidity"] = temperature.humidity!!.toDouble()
                map["pressure"] = temperature.pressure!!.toDouble()

                allValues.add(map)
            }
        }

        return ResponseEntity(allValues, HttpStatus.OK)
    }

    private fun doStuff(today: LocalDateTime, hourlyAverages: ConcurrentSkipListMap<LocalDateTime, Number>) {
        val average = service!!.getTemperatures(today, today.plus(1, ChronoUnit.HOURS))
                .parallelStream()
                .mapToDouble { c -> c!!.temperature!!.toDouble() }
                .average()

        hourlyAverages[today] = average.orElseGet { 0.0 }
//        log.info("Looking for: " + today + " and : " + today.plus(1, ChronoUnit.HOURS) + "  - Average: " + if (average.isEmpty) 0 else average.asDouble)
    }
}
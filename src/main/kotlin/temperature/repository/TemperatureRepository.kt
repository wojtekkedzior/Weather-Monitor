package temperature.repository

import temperature.model.Temperature
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime

interface TemperatureRepository : CrudRepository<Temperature?, Long?> {
    @Query(value = "SELECT * FROM iot.temperature ORDER BY id DESC LIMIT ?1 ", nativeQuery = true)
    fun findLastTemperatures(duration: Int): List<Temperature?>?

    fun findLastTemperaturesByTimestamp(date: LocalDateTime?): List<Temperature?>?
    fun findTemperaturesByTimestampBetween(start: LocalDateTime?, end: LocalDateTime?): List<Temperature?>?
    fun findTopByOrderByIdDesc(): Temperature? //    User findTopByOrderByAgeDesc();
}
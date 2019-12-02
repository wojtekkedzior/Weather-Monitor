package com.iot.temperature.model

import lombok.Data
import java.time.LocalDateTime

@Data
class TempPerHour {
    private val hour: LocalDateTime? = null
    private val average: Number? = null
}
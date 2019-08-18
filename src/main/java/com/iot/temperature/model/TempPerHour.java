package com.iot.temperature.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TempPerHour {

    private final LocalDateTime hour;
    private final Number average;
}

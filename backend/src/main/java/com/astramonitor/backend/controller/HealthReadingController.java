package com.astramonitor.backend.controller;

import com.astramonitor.backend.entity.HealthReading;
import com.astramonitor.backend.service.HealthReadingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health-readings")
public class HealthReadingController {
    private final HealthReadingService healthReadingService;

    public HealthReadingController(HealthReadingService healthReadingService) {
        this.healthReadingService = healthReadingService;
    }

    @PostMapping
    public HealthReading receiveHealthReading(@RequestBody HealthReading healthReading) {
        return healthReadingService.saveHealthReading(healthReading);
    }
}

package com.astramonitor.backend.controller;

import com.astramonitor.backend.dto.request.HealthReadingRequestDto;
import com.astramonitor.backend.entity.HealthReading;
import com.astramonitor.backend.service.HealthReadingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/health-readings")
public class HealthReadingController {
    private final HealthReadingService healthReadingService;

    public HealthReadingController(HealthReadingService healthReadingService) {
        this.healthReadingService = healthReadingService;
    }

    @PostMapping
    public HealthReading receiveHealthReading(@RequestBody HealthReadingRequestDto request) {
        return healthReadingService.saveHealthReading(request);
    }

}

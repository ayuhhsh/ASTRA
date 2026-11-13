package com.astramonitor.backend.controller;

import com.astramonitor.backend.dto.request.HealthReadingRequestDto;
import com.astramonitor.backend.dto.response.HealthReadingResponseDto;
import com.astramonitor.backend.entity.HealthReading;
import com.astramonitor.backend.service.HealthReadingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health-readings")
public class HealthReadingController {
    private final HealthReadingService healthReadingService;

    public HealthReadingController(HealthReadingService healthReadingService) {
        this.healthReadingService = healthReadingService;
    }

    @PostMapping
    public ResponseEntity<HealthReadingResponseDto> receiveHealthReading(@Valid @RequestBody HealthReadingRequestDto request) {
        HealthReadingResponseDto response = healthReadingService.saveHealthReading(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

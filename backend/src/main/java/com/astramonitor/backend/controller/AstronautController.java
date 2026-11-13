package com.astramonitor.backend.controller;

import com.astramonitor.backend.dto.response.*;
import com.astramonitor.backend.entity.Astronaut;
import com.astramonitor.backend.entity.HealthReading;
import com.astramonitor.backend.entity.RiskAssessment;
import com.astramonitor.backend.service.*;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/astronauts")
public class AstronautController {

    private final AstronautService astronautService;
    private final HealthReadingService healthReadingService;
    private final RiskAssessmentService riskAssessmentService;

    public AstronautController(
            AstronautService astronautService,
            HealthReadingService healthReadingService,
            RiskAssessmentService riskAssessmentService) {
        this.astronautService = astronautService;
        this.healthReadingService = healthReadingService;
        this.riskAssessmentService = riskAssessmentService;
    }

    @GetMapping
    public List<AstronautResponseDto> getAllAstronauts() {
        return astronautService.getAllAstronauts()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @GetMapping("/{id}")
    public AstronautResponseDto getAstronautById(
            @PathVariable Long id) {
        return convertToDto(astronautService.getAstronautById(id));
    }

    @PostMapping
    public AstronautResponseDto saveAstronaut(
            @RequestBody Astronaut astronaut) {
        return convertToDto(astronautService.saveAstronaut(astronaut));
    }

    @GetMapping("/{id}/health")
    public List<HealthReadingResponseDto> getHealthHistory(
            @PathVariable Long id) {
        return healthReadingService.getHealthHistory(id)
                .stream()
                .map(this::convertHealthToDto)
                .toList();
    }

    @GetMapping("/{id}/risk")
    public RiskAssessmentResponseDto getLatestRisk(
            @PathVariable Long id) {
        return convertRiskToDto(
                riskAssessmentService.getLatestRisk(id));
    }

    private AstronautResponseDto convertToDto(Astronaut astronaut) {
        return new AstronautResponseDto(
                astronaut.getId(),
                astronaut.getName(),
                astronaut.getMission(),
                astronaut.getStatus()
        );
    }

    private HealthReadingResponseDto convertHealthToDto(
            HealthReading reading) {
        return new HealthReadingResponseDto(
                reading.getId(),
                reading.getAstronaut().getId(),
                reading.getAstronaut().getName(),
                reading.getAstronaut().getMission(),
                reading.getHeartRate(),
                reading.getSpo2(),
                reading.getTemperature(),
                reading.getStressLevel(),
                reading.getRecordedAt()
        );
    }

    private RiskAssessmentResponseDto convertRiskToDto(
            RiskAssessment assessment) {
        return new RiskAssessmentResponseDto(
                assessment.getId(),
                assessment.getAstronaut().getId(),
                assessment.getRiskScore(),
                assessment.getRiskLevel(),
                assessment.getAnomalyDetected(),
                assessment.getReason(),
                assessment.getCreatedAt()
        );
    }
}
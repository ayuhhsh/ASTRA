package com.astramonitor.backend.controller;

import com.astramonitor.backend.entity.Astronaut;
import com.astramonitor.backend.entity.HealthReading;
import com.astramonitor.backend.entity.RiskAssessment;
import com.astramonitor.backend.service.AstronautService;

import com.astramonitor.backend.service.HealthReadingService;
import com.astramonitor.backend.service.RiskAssessmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/astronauts")
public class AstronautController {

    private final AstronautService astronautService;
    private final HealthReadingService healthReadingService;
    private final RiskAssessmentService riskAssessmentService;

    public AstronautController(AstronautService astronautService, HealthReadingService healthReadingService, RiskAssessmentService riskAssessmentService) {
        this.astronautService = astronautService;
        this.healthReadingService = healthReadingService;
        this.riskAssessmentService = riskAssessmentService;
    }

    @GetMapping
    public List<Astronaut> getAllAstronauts() {
        return astronautService.getAllAstronauts();
    }

    @GetMapping("/{astronautId}/health")
    public List<HealthReading> getHealthHistory(
            @PathVariable Long astronautId) {
        return healthReadingService.getHealthHistory(astronautId);
    }
    @GetMapping("/{id}")
    public Astronaut getAstronautById(@PathVariable Long id) {
        return astronautService.getAstronautById(id);
    }

    @PostMapping
    public Astronaut saveAstronaut(@RequestBody Astronaut astronaut) {
        return astronautService.saveAstronaut(astronaut);
    }
    @GetMapping("/{id}/risk")
    public RiskAssessment getLatestRisk(@PathVariable Long id) {
        return riskAssessmentService.getLatestRisk(id);
    }


}
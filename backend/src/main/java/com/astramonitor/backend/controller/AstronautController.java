package com.astramonitor.backend.controller;

import com.astramonitor.backend.entity.Astronaut;
import com.astramonitor.backend.entity.HealthReading;
import com.astramonitor.backend.service.AstronautService;

import com.astramonitor.backend.service.HealthReadingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/astronauts")
public class AstronautController {

    private final AstronautService astronautService;
    private final HealthReadingService healthReadingService;

    public AstronautController(AstronautService astronautService, HealthReadingService healthReadingService) {
        this.astronautService = astronautService;
        this.healthReadingService = healthReadingService;
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
}
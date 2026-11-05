package com.astramonitor.backend.service;

import com.astramonitor.backend.dto.request.HealthReadingRequestDto;
import com.astramonitor.backend.dto.response.HealthReadingResponseDto;
import com.astramonitor.backend.entity.Astronaut;
import com.astramonitor.backend.entity.HealthReading;
import com.astramonitor.backend.entity.RiskAssessment;
import com.astramonitor.backend.repository.AstronautRepository;
import com.astramonitor.backend.repository.HealthReadingRepository;

import com.astramonitor.backend.service.RiskAssessmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HealthReadingService {

    private final HealthReadingRepository healthReadingRepository;
    private final AstronautRepository astronautRepository;
    private final RiskAssessmentService riskAssessmentService;

    public HealthReadingService(
            HealthReadingRepository healthReadingRepository,
            AstronautRepository astronautRepository,
            RiskAssessmentService riskAssessmentService) {
        this.healthReadingRepository = healthReadingRepository;
        this.astronautRepository = astronautRepository;
        this.riskAssessmentService = riskAssessmentService;
    }

    @Transactional
    public HealthReadingResponseDto saveHealthReading(
            HealthReadingRequestDto request) {

        Astronaut astronaut = astronautRepository
                .findById(request.getAstronautId())
                .orElseThrow(() -> new RuntimeException(
                        "Astronaut not found with ID: "
                                + request.getAstronautId()));

        HealthReading healthReading = new HealthReading();

        healthReading.setAstronaut(astronaut);
        healthReading.setHeartRate(request.getHeartRate());
        healthReading.setSpo2(request.getSpo2());
        healthReading.setTemperature(request.getTemperature());
        healthReading.setStressLevel(request.getStressLevel());
        healthReading.setRecordedAt(LocalDateTime.now());

        HealthReading savedReading =
                healthReadingRepository.save(healthReading);

        riskAssessmentService.assessRisk(savedReading);

        return new HealthReadingResponseDto(
                savedReading.getId(),
                astronaut.getId(),
                astronaut.getName(),
                astronaut.getMission(),
                savedReading.getHeartRate(),
                savedReading.getSpo2(),
                savedReading.getTemperature(),
                savedReading.getStressLevel(),
                savedReading.getRecordedAt()
        );
    }

    public List<HealthReading> getHealthHistory(Long astronautId) {

        if (!astronautRepository.existsById(astronautId)) {
            throw new RuntimeException(
                    "Astronaut not found with ID: " + astronautId);
        }

        return healthReadingRepository
                .findByAstronaut_IdOrderByRecordedAtDesc(astronautId);
    }
}

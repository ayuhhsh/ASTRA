package com.astramonitor.backend.service;

import com.astramonitor.backend.entity.HealthReading;
import com.astramonitor.backend.repository.HealthReadingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HealthReadingService {

    private final HealthReadingRepository healthReadingRepository;

    public HealthReadingService(HealthReadingRepository healthReadingRepository) {
        this.healthReadingRepository = healthReadingRepository;
    }

    public HealthReading saveHealthReading(HealthReading healthReading){
        healthReading.setRecordedAt(LocalDateTime.now());

        return healthReadingRepository.save(healthReading);
    }
}

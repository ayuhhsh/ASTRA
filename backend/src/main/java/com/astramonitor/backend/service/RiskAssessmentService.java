package com.astramonitor.backend.service;

import com.astramonitor.backend.entity.HealthReading;
import com.astramonitor.backend.entity.RiskAssessment;
import com.astramonitor.backend.repository.HealthReadingRepository;
import com.astramonitor.backend.repository.RiskAssessmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RiskAssessmentService {

    private final HealthReadingRepository healthReadingRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final AlertService alertService;


    public RiskAssessmentService(HealthReadingRepository healthReadingRepository, RiskAssessmentRepository riskAssessmentRepository, AlertService alertService) {
        this.healthReadingRepository = healthReadingRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.alertService = alertService;
    }

    @Transactional
    public RiskAssessment assessRisk(HealthReading reading) {

        int score = 0;
        StringBuilder reason = new StringBuilder();

        if (reading.getHeartRate() != null) {
            if (reading.getHeartRate() < 50
                    || reading.getHeartRate() > 120) {
                score += 35;
                reason.append("Abnormal heart rate; ");
            }
        }
        if (reading.getSpo2() != null && reading.getSpo2() < 90) {
            score += 40;
            reason.append("Low SpO2; ");
        }

        if (reading.getTemperature() != null) {
            if (reading.getTemperature() < 35.0
                    || reading.getTemperature() > 38.5) {
                score += 30;
                reason.append("Abnormal temperature; ");
            }
        }

        if (reading.getStressLevel() != null
                && reading.getStressLevel() >= 8) {
            score += 20;
            reason.append("High stress; ");
        }

        score = Math.min(score, 100);

        String level;
        if (score >= 70) {
            level = "CRITICAL";
        } else if (score >= 40) {
            level = "HIGH";
        } else if (score >= 20) {
            level = "MODERATE";
        } else {
            level = "LOW";
        }

        RiskAssessment assessment = new RiskAssessment();
        assessment.setAstronaut(reading.getAstronaut());
        assessment.setRiskScore((double) score);
        assessment.setRiskLevel(level);
        assessment.setAnomalyDetected(score >= 40);
        assessment.setReason(
                reason.length() == 0
                        ? "No threshold violations detected"
                        : reason.toString()
        );
        assessment.setCreatedAt(LocalDateTime.now());


        RiskAssessment savedAssessment =
                riskAssessmentRepository.save(assessment);

        alertService.createAlert(savedAssessment);

        return savedAssessment;
    }
}

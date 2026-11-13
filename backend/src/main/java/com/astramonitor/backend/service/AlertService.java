package com.astramonitor.backend.service;

import com.astramonitor.backend.entity.Alert;
import com.astramonitor.backend.entity.RiskAssessment;
import com.astramonitor.backend.exception.ResourceNotFoundException;
import com.astramonitor.backend.repository.AlertRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {
    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Alert> getAlertsByStatus(String status) {
        return alertRepository
                .findByStatusOrderByCreatedAtDesc(status);
    }

    @Transactional
    public Alert createAlert(RiskAssessment assessment) {
        String level = assessment.getRiskLevel();

        if (!"HIGH".equals(level) && !"CRITICAL".equals(level)) {
            return null;
        }

        Alert alert = new Alert();
        alert.setAstronaut(assessment.getAstronaut());
        alert.setSeverity(level);
        alert.setMessage(
                "Risk detected: " + level + ". "
                        + assessment.getReason()
        );
        alert.setStatus("OPEN");
        alert.setCreatedAt(LocalDateTime.now());

        return alertRepository.save(alert);
    }

    @Transactional
    public Alert updateAlertStatus(Long id, String status) {
        if (!List.of("OPEN", "ACKNOWLEDGED", "RESOLVED")
                .contains(status)) {
            throw new IllegalArgumentException(
                    "Invalid status: " + status
            );
        }

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Alert not found with ID: " + id
                ));

        alert.setStatus(status);
        return alertRepository.save(alert);
    }
}

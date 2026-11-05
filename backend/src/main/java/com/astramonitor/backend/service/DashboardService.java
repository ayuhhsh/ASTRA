package com.astramonitor.backend.service;

import com.astramonitor.backend.dto.response.DashboardSummaryDto;
import com.astramonitor.backend.repository.AlertRepository;
import com.astramonitor.backend.repository.AstronautRepository;
import com.astramonitor.backend.repository.RiskAssessmentRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final AstronautRepository astronautRepository;
    private final AlertRepository alertRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;

    public DashboardService(AstronautRepository astronautRepository, AlertRepository alertRepository, RiskAssessmentRepository riskAssessmentRepository) {
        this.astronautRepository = astronautRepository;
        this.alertRepository = alertRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;
    }

    public DashboardSummaryDto getSummary() {
        return new DashboardSummaryDto(
                astronautRepository.count(),
                riskAssessmentRepository.countByRiskLevel("HIGH"),
                riskAssessmentRepository.countByRiskLevel("CRITICAL"),
                alertRepository.countByStatus("OPEN")
        );
    }
}

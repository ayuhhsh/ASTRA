package com.astramonitor.backend.controller;

import com.astramonitor.backend.dto.response.RiskAssessmentResponseDto;
import com.astramonitor.backend.entity.RiskAssessment;
import com.astramonitor.backend.service.RiskAssessmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/risk-assessments")
public class RiskAssessmentController {

    private final RiskAssessmentService riskAssessmentService;

    public RiskAssessmentController(
            RiskAssessmentService riskAssessmentService) {
        this.riskAssessmentService = riskAssessmentService;
    }

    @GetMapping("/astronaut/{astronautId}/latest")
    public RiskAssessmentResponseDto getLatestRisk(
            @PathVariable Long astronautId) {

        RiskAssessment assessment =
                riskAssessmentService.getLatestRisk(astronautId);

        return convertToDto(assessment);
    }

    @GetMapping("/astronaut/{astronautId}")
    public List<RiskAssessmentResponseDto> getRiskHistory(
            @PathVariable Long astronautId) {

        return riskAssessmentService
                .getRiskHistory(astronautId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    private RiskAssessmentResponseDto convertToDto(
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

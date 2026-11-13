package com.astramonitor.backend.dto.response;

import java.time.LocalDateTime;

public record RiskAssessmentResponseDto(
        Long id,
        Long astronautId,
        Double riskScore,
        String riskLevel,
        Boolean anomalyDetected,
        String reason,
        LocalDateTime createdAt
) {
}
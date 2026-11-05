package com.astramonitor.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardSummaryDto {
    private long totalAstronauts;
    private long highRiskCount;
    private long criticalRiskCount;
    private long openAlerts;
}

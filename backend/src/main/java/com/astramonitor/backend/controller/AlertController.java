package com.astramonitor.backend.controller;

import com.astramonitor.backend.dto.response.AlertResponseDto;
import com.astramonitor.backend.entity.Alert;
import com.astramonitor.backend.service.AlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public List<AlertResponseDto> getAllAlerts() {
        return alertService.getAllAlerts()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @GetMapping("/status/{status}")
    public List<AlertResponseDto> getAlertsByStatus(
            @PathVariable String status) {

        return alertService.getAlertsByStatus(status.toUpperCase())
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @PatchMapping("/{id}/status")
    public AlertResponseDto updateAlertStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String status = request.get("status");

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }

        Alert alert = alertService.updateAlertStatus(
                id, status.toUpperCase()
        );

        return convertToDto(alert);
    }

    private AlertResponseDto convertToDto(Alert alert) {
        return new AlertResponseDto(
                alert.getId(),
                alert.getAstronaut().getId(),
                alert.getAstronaut().getName(),
                alert.getAstronaut().getMission(),
                alert.getSeverity(),
                alert.getMessage(),
                alert.getStatus(),
                alert.getCreatedAt()
        );
    }
}

package com.astramonitor.backend.controller;

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
    public List<Alert> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/status/{status}")
    public List<Alert> getAlertsByStatus(
            @PathVariable String status) {
        return alertService.getAlertsByStatus(
                status.toUpperCase()
        );
    }

    @PatchMapping("/{id}/status")
    public Alert updateAlertStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String status = request.get("status");

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException(
                    "Status is required"
            );
        }

        return alertService.updateAlertStatus(
                id, status.toUpperCase()
        );
    }
}

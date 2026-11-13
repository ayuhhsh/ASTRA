package com.astramonitor.backend.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

    @Data
    public class MLPredictionResponseDto {

        private Long id;
        private Long astronautId;
        private Boolean anomalyDetected;
        private String riskLevel;
        private Double anomalyScore;
        private Double threshold;
        private String thresholdMethod;
        private String dataSource;
        private String trainingDataSource;
        private String message;
        private LocalDateTime recordedAt;
}

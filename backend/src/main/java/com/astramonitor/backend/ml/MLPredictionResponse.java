package com.astramonitor.backend.ml;

import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class MLPredictionResponse {

        private Long astronautId;
        private String recordedAt;
        private String dataSource;
        private String trainingDataSource;

        private boolean anomalyDetected;
        private String riskLevel;
        private double anomalyScore;
        private double threshold;
        private String thresholdMethod;

        private String message;
        private String disclaimer;

}

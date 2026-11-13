
package com.astramonitor.backend.service;

import com.astramonitor.backend.dto.response.MLPredictionResponseDto;
import com.astramonitor.backend.entity.Astronaut;
import com.astramonitor.backend.entity.MLPrediction;
import com.astramonitor.backend.ml.MLPredictionResponse;
import com.astramonitor.backend.repository.MLPredictionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class MLPredictionService {

    private final MLPredictionRepository mlPredictionRepository;

    public MLPredictionService(
            MLPredictionRepository mlPredictionRepository) {
        this.mlPredictionRepository = mlPredictionRepository;
    }

    public MLPrediction savePrediction(
            Astronaut astronaut,
            MLPredictionResponse response) {

        MLPrediction prediction = new MLPrediction();

        prediction.setAstronaut(astronaut);
        prediction.setAnomalyDetected(response.isAnomalyDetected());
        prediction.setRiskLevel(response.getRiskLevel());
        prediction.setAnomalyScore(response.getAnomalyScore());
        prediction.setThreshold(response.getThreshold());
        prediction.setThresholdMethod(response.getThresholdMethod());
        prediction.setDataSource(response.getDataSource());
        prediction.setTrainingDataSource(response.getTrainingDataSource());
        prediction.setMessage(response.getMessage());

        prediction.setRecordedAt(
                response.getRecordedAt() != null
                        ? OffsetDateTime.parse(response.getRecordedAt())
                        .toLocalDateTime()
                        : LocalDateTime.now()
        );

        return mlPredictionRepository.save(prediction);
    }

    public List<MLPredictionResponseDto> getPredictions(Long astronautId) {

        return mlPredictionRepository
                .findByAstronaut_IdOrderByRecordedAtDesc(astronautId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    private MLPredictionResponseDto convertToDto(MLPrediction prediction) {

        MLPredictionResponseDto dto = new MLPredictionResponseDto();

        dto.setId(prediction.getId());
        dto.setAstronautId(prediction.getAstronaut().getId());
        dto.setAnomalyDetected(prediction.getAnomalyDetected());
        dto.setRiskLevel(prediction.getRiskLevel());
        dto.setAnomalyScore(prediction.getAnomalyScore());
        dto.setThreshold(prediction.getThreshold());
        dto.setThresholdMethod(prediction.getThresholdMethod());
        dto.setDataSource(prediction.getDataSource());
        dto.setTrainingDataSource(prediction.getTrainingDataSource());
        dto.setMessage(prediction.getMessage());
        dto.setRecordedAt(prediction.getRecordedAt());

        return dto;
    }
}

package com.astramonitor.backend.controller;

import com.astramonitor.backend.dto.response.MLPredictionResponseDto;
import com.astramonitor.backend.entity.MLPrediction;
import com.astramonitor.backend.service.MLPredictionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ml-predictions")
public class MLPredictionController {

    private final MLPredictionService mlPredictionService;

    public MLPredictionController(MLPredictionService mlPredictionService) {
        this.mlPredictionService = mlPredictionService;
    }

    @GetMapping("/astronaut/{astronautId}")
    public List<MLPredictionResponseDto> getPredictions(@PathVariable Long astronautId){

        return mlPredictionService.getPredictions(astronautId);
    }
}

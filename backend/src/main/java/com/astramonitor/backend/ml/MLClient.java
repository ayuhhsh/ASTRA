package com.astramonitor.backend.ml;

import com.astramonitor.backend.dto.request.HealthReadingRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MLClient {

        private final RestClient restClient;

    public MLClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8000")
                .build();
    }

    public MLPredictionResponse predict(HealthReadingRequestDto request) {
        System.out.println("Calling ASTRA ML API...");

        MLPredictionResponse response = restClient.post()
                .uri("/predict")
                .body(request)
                .retrieve()
                .body(MLPredictionResponse.class);

        System.out.println("ML Prediction received: " + response);

        return response;
    }
}

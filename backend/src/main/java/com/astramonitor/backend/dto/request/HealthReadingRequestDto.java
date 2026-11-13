package com.astramonitor.backend.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class HealthReadingRequestDto {

    @NotNull(message = "Astronaut ID is required")
    @Positive(message = "Astronaut ID must be positive")
    private Long astronautId;

    @NotNull(message = "Heart rate is required")
    @Positive(message = "Heart rate must be greater than zero")
    private Integer heartRate;

    @NotNull(message = "SpO2 is required")
    @Min(value = 0, message = "SpO2 cannot be less than 0")
    @Max(value = 100, message = "SpO2 cannot be greater than 100")
    private Integer spo2;

    @NotNull(message = "Temperature is required")
    @DecimalMin(value = "20.0", message = "Temperature cannot be below 20°C")
    @DecimalMax(value = "50.0", message = "Temperature cannot exceed 50°C")
    private Double temperature;

    @NotNull(message = "Stress level is required")
    @Min(value = 0, message = "Stress level cannot be less than 0")
    @Max(value = 10, message = "Stress level cannot exceed 10")
    private Integer stressLevel;
}
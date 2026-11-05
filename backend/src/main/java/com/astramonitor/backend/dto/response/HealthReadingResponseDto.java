package com.astramonitor.backend.dto.response;

import java.time.LocalDateTime;

public record HealthReadingResponseDto (
    Long id,
    Long astronautId,
    String astronautName,
    String mission,
    Integer heartRate,
    Integer spo2,
    Double temperature,
    Integer stressLevel,
    LocalDateTime recordedAt
){

}

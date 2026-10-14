package com.astramonitor.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class HealthReadingRequestDto {

    private Long astronautId;

    private Integer heartRate;

    private Integer spo2;

    private Double temperature;

    private Integer stressLevel;

}

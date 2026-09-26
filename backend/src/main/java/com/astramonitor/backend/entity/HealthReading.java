package com.astramonitor.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_reading")
@Setter
@Getter
@NoArgsConstructor
public class HealthReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long astronautId;

    private Integer heartRate;

    private Integer spo2;

    private Double temperature;

    private Integer stressLevel;

    private LocalDateTime recordedAt;
}

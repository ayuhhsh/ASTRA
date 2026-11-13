
package com.astramonitor.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ml_prediction")
@Getter
@Setter
@NoArgsConstructor
public class MLPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "astronaut_id", nullable = false)
    private Astronaut astronaut;

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

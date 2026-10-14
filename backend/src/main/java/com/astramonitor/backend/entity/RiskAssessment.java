package com.astramonitor.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "risk_assessment")
@Getter
@Setter
@NoArgsConstructor
public class RiskAssessment {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "astronaut_id", nullable = false)
private Astronaut astronaut;

private Double riskScore;

private String riskLevel;

private Boolean anomalyDetected;

private String reason;

private LocalDateTime createdAt;
}
    package com.astramonitor.backend.entity;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.time.LocalDateTime;

    @Entity
    @Table(name = "health_reading")
    @Getter
    @Setter
    @NoArgsConstructor
    public class HealthReading {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "astronaut_id", nullable = false)
        private Astronaut astronaut;

        private Integer heartRate;

        private Integer spo2;

        private Double temperature;

        private Integer stressLevel;

        private LocalDateTime recordedAt;
    }
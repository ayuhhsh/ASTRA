package com.astramonitor.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

    @Entity
    @Table(name = "alert")
    @Getter
    @Setter
    @NoArgsConstructor
    public class Alert {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "astronaut_id", nullable = false)
        private Astronaut astronaut;

        private String severity;

        @Column(length = 1000)
        private String message;

        private String status;

        private LocalDateTime createdAt;
}

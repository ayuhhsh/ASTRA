package com.astramonitor.backend.repository;

import com.astramonitor.backend.entity.HealthReading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthReadingRepository extends JpaRepository<HealthReading, Long> {

}

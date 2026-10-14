package com.astramonitor.backend.repository;

import com.astramonitor.backend.entity.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {

    List<RiskAssessment> findByAstronaut_IdOrderByCreatedAtDesc(
            Long astronautId
    );
}

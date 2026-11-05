package com.astramonitor.backend.repository;

import com.astramonitor.backend.entity.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {

    List<RiskAssessment> findByAstronaut_IdOrderByCreatedAtDesc(
            Long astronautId
    );
    Optional<RiskAssessment> findFirstByAstronaut_IdOrderByCreatedAtDesc(
            Long astronautId
    );

    long countByRiskLevel(String riskLevel);
}

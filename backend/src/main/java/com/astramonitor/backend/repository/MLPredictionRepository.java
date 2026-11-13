
package com.astramonitor.backend.repository;

import com.astramonitor.backend.entity.MLPrediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MLPredictionRepository
        extends JpaRepository<MLPrediction, Long> {

    List<MLPrediction> findByAstronaut_IdOrderByRecordedAtDesc(
            Long astronautId
    );
}

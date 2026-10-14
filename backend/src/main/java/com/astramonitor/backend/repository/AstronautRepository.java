package com.astramonitor.backend.repository;

import com.astramonitor.backend.entity.Astronaut;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AstronautRepository extends JpaRepository<Astronaut, Long> {

}

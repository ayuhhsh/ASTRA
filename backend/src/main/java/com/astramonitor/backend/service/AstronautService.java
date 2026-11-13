package com.astramonitor.backend.service;

import com.astramonitor.backend.entity.Astronaut;
import com.astramonitor.backend.exception.ResourceNotFoundException;
import com.astramonitor.backend.repository.AstronautRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AstronautService {

    private final AstronautRepository astronautRepository;


    public AstronautService(AstronautRepository astronautRepository) {
        this.astronautRepository = astronautRepository;
    }

    public Astronaut saveAstronaut(Astronaut astronaut){
        return astronautRepository.save(astronaut);
    }

    public List<Astronaut> getAllAstronauts() {
        return astronautRepository.findAll();
    }
    public Astronaut getAstronautById(Long id) {
        return astronautRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Astronaut not found with ID: " + id
                        )
                );
    }
}

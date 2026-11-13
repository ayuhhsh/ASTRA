package com.astramonitor.backend.dto.response;

public record AstronautResponseDto(
        Long id,
        String name,
        String mission,
        String status
) {
}
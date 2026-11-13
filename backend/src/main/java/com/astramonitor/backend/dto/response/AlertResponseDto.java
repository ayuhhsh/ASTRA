package com.astramonitor.backend.dto.response;

import java.time.LocalDateTime;

public record AlertResponseDto(
        Long id,
        Long astronautId,
        String astronautName,
        String mission,
        String severity,
        String message,
        String status,
        LocalDateTime createdAt
) {
}
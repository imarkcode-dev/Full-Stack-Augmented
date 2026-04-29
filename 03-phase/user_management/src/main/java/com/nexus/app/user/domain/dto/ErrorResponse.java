package com.nexus.app.user.domain.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
     LocalDateTime timestamp,
     int status,
     String message
) {}

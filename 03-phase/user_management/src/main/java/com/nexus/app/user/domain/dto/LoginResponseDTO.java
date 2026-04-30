package com.nexus.app.user.domain.dto;

import java.time.LocalDateTime;

public record LoginResponseDTO(
    
    Integer userId,
    String name,
    LocalDateTime lastLogin

) {}

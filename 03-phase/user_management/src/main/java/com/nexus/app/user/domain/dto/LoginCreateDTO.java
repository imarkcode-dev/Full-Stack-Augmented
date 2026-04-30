package com.nexus.app.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record LoginCreateDTO(
    @NotNull(message = "userId is required")
    Integer userId,

    @NotBlank(message = "username is required")
    String username,

    @NotBlank(message = "password is required")
    String password
) {}

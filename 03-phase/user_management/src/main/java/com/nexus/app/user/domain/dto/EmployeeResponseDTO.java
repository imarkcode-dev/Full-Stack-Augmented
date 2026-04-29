// Create a Java record named EmployeeResponseDTO
// Fields: id, name, lastName, email, phone, createdAt, updatedAt
// Types: Integer, String, LocalDateTime

package com.nexus.app.user.domain.dto;

import java.time.LocalDateTime;

public record EmployeeResponseDTO(
    Integer id,
    String name,
    String lastName,
    String email,
    String phone,
    LocalDateTime createdAt,
    LocalDateTime updatedAt

) {}

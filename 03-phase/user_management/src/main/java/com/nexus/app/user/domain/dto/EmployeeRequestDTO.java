// Create a Java record named EmployeeRequestDTO
// Fields: name, lastName, email, phone
// Use String types

package com.nexus.app.user.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record EmployeeRequestDTO (

    @NotBlank
    String name,

    @NotBlank
    String lastName,
    
    @NotBlank
    String email,
    
    String phone
    
) {}

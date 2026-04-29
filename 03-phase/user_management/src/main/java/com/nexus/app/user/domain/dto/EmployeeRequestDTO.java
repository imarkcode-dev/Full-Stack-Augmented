// Create a Java record named EmployeeRequestDTO
// Fields: name, lastName, email, phone
// Use String types

package com.nexus.app.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmployeeRequestDTO (

    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Last Name is required")
    String lastName,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email")
    String email,
    
    String phone
    
) {}

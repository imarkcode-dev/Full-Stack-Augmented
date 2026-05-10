package com.smart.billing.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InvoiceRequestDTO(

    @NotNull(message = "Contract ID is required")
    Integer contractId,

    @NotBlank(message = "Invoice number is required")
    String invoiceNumber,

    @NotNull(message = "Issue date is required")
    LocalDate issueDate,

    @NotNull(message = "Due date is required")
    LocalDate dueDate,

    BigDecimal penaltyAmount

    
) { }

package com.smart.billing.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvoiceResponseDTO(
    Integer id,
    String invoiceNumber,
    String customerName,
    String contractTitle,
    LocalDate issueDate,
    LocalDate dueDate,
    BigDecimal totalAmount,
    BigDecimal penaltyAmount,
    String status
    
) { }

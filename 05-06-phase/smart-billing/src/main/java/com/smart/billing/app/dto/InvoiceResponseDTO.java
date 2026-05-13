package com.smart.billing.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceResponseDTO(
    Integer id,
    String invoiceNumber,
    String customerName,
    String contractTitle,
    LocalDateTime issueDate,
    LocalDateTime dueDate,
    BigDecimal totalAmount,
    BigDecimal penaltyAmount,
    String status
    
) { }

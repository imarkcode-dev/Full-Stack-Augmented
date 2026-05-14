package com.smart.billing.app.dto;

import java.math.BigDecimal;
import java.util.Map;

public record DashboardResponseDTO(
    BigDecimal totalInvoiced,    
    BigDecimal collectedRevenue, 
    BigDecimal overdueAmount,    
    Map<String, BigDecimal> cashFlowForecast 
) 
{}

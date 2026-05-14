package com.smart.billing.app.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.smart.billing.app.dto.DashboardResponseDTO;
import com.smart.billing.app.repository.DashboardRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for handling dashboard-related business logic.
 *
 * This service aggregates key financial metrics and analytical data to provide
 * a high-level summary of the application's status. It facilitates the
 * retrieval of total invoiced amounts, total collections, overdue debt,
 * and cash flow forecasting.
 *
 * @author Smart Billing Team
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final DashboardRepository dashboardRepository;

    /**
     * Retrieves a summary of financial metrics for the dashboard view.
     *
     * This method fetches aggregated data for total invoiced amounts,
     * total payments collected, and current overdue amounts. It also
     * generates a monthly cash flow forecast based on historical payment data.
     * Null values from the repository are safely handled and defaulted to zero.
     *
     * @return DashboardResponseDTO containing the aggregated financial metrics
     */
    @Override
    public DashboardResponseDTO getSummary() {

        BigDecimal totalInvoiced = dashboardRepository.getTotalInvoiced();
        BigDecimal collected = dashboardRepository.getTotalCollected();
        BigDecimal overdue = dashboardRepository.getOverdueAmount();
        
        Map<String, BigDecimal> forecast = dashboardRepository.getMonthlyCashFlow().stream()
            .collect(Collectors.toMap(
                obj -> obj[0].toString(),
                obj -> (BigDecimal) obj[1]
            ));

        return new DashboardResponseDTO(
            totalInvoiced != null ? totalInvoiced : BigDecimal.ZERO,
            collected != null ? collected : BigDecimal.ZERO,
            overdue != null ? overdue : BigDecimal.ZERO,
            forecast
        );
    }



}

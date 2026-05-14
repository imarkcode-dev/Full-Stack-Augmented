import { Component, signal, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';

import { DashboardService } from '../../core/services/dashboard.service';
import { DashboardResponse } from '../../models/dashboard.model';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, MatCardModule, MatProgressBarModule, MatIconModule, MatButtonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit {

  private dashboardService = inject(DashboardService);

  totalInvoiced = signal<number>(0);
  totalCollected = signal<number>(0);
  overdueAmount = signal<number>(0);

  cashFlowData = signal<Record<string, number>>({});


  ngOnInit(): void {
    this.loadFinancialMetrics();
  }

 loadFinancialMetrics() {
    this.dashboardService.getSummary().subscribe({
      next: (data: DashboardResponse) => {
        this.totalInvoiced.set(data.totalInvoiced);
        this.totalCollected.set(data.totalCollected);
        this.overdueAmount.set(data.overdueAmount);
        this.cashFlowData.set(data.cashFlowForecast);
      },
      error: (err) => {
        console.error('Error fetching dashboard summary metrics:', err);
      }
    });
  } 

}

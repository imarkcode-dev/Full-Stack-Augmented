import { Component, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, MatCardModule, MatProgressBarModule, MatIconModule, MatButtonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit {

  private invoicedTotal = 15000;
  private revenueCollected = 12000;
  

  ngOnInit(): void {
    // Here we would load the actual data from a DashboardService
  }

  totalInvoiced(): number {
    return this.invoicedTotal;
  }

  collectedRevenue(): number {
    return this.revenueCollected;
  }

  overdueAmount(): number {
    return 8000;
  }

}

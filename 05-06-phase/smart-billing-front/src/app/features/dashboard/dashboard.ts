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


  totalInvoiced = signal<number>(0);
  collectedRevenue = signal<number>(0);
  overdueAmount = signal<number>(0);


  ngOnInit(): void {
    this.loadFinancialMetrics();
  }

  loadFinancialMetrics() {
   
    setTimeout(() => {
      this.totalInvoiced.set(258400.50);
      this.collectedRevenue.set(185200.00);
      this.overdueAmount.set(73200.50);
    }, 800);
  }

}

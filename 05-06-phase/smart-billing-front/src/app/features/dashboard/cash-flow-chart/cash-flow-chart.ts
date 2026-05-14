import { CommonModule } from '@angular/common';
import { Component, ElementRef, Input, OnDestroy, OnInit, ViewChild, effect } from '@angular/core';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-cash-flow-chart',
  imports: [CommonModule],
  templateUrl: './cash-flow-chart.html',
  styleUrl: './cash-flow-chart.scss',
})
export class CashFlowChartTs implements OnInit, OnDestroy {

  @ViewChild('chartCanvas', { static: true }) chartCanvas!: ElementRef<HTMLCanvasElement>;

  private _data: Record<string, number> = {};
  private chart!: Chart;

  // Usamos un setter para detectar cambios en el Input de forma reactiva
  @Input({ required: true }) 
  set data(value: Record<string, number>) {
    this._data = value;
    if (this.chart) {
      this.updateChart();
    }
  }

  get data(): Record<string, number> {
    return this._data;
  }

  ngOnInit(): void {
    this.buildChart();
  }

  ngOnDestroy(): void {
    // Buena práctica: destruir la instancia del chart para evitar memory leaks
    if (this.chart) {
      this.chart.destroy();
    }
  }

  private buildChart(): void {
    const labels = Object.keys(this.data);
    const values = Object.values(this.data);

    this.chart = new Chart(this.chartCanvas.nativeElement, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Collected Revenue ($)',
            data: values,
            backgroundColor: 'rgba(63, 81, 181, 0.7)', 
            borderColor: '#3f51b5',
            borderWidth: 1,
            borderRadius: 6,
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: (value) => '$' + Number(value).toLocaleString()
            }
          }
        },
        plugins: {
          legend: {
            display: true,
            position: 'top'
          }
        }
      }
    });
  }

  private updateChart(): void {
    // Actualiza los datos dinámicamente sin destruir el canvas de la UI
    this.chart.data.labels = Object.keys(this.data);
    this.chart.data.datasets[0].data = Object.values(this.data);
    this.chart.update();
  }

}

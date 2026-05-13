import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common'; 
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';

import { InvoiceService } from '../../../core/services/invoice.service';
import { InvoiceResponse } from '../../../models/invoice.model';
import { Invoice } from '../invoice/invoice';

@Component({
  selector: 'app-invoice-list',
  standalone: true,
  imports: [
    CommonModule, 
    MatTableModule, 
    MatButtonModule, 
    MatIconModule, 
    MatDialogModule,
    MatTooltipModule,
    CurrencyPipe
  ],
  templateUrl: './invoice-list.html',
  styleUrl: './invoice-list.scss',
})
export class InvoiceList implements OnInit { 

  private invoiceService = inject(InvoiceService);
  private dialog = inject(MatDialog);

  invoices = signal<InvoiceResponse[]>([]);
  displayedColumns: string[] = ['number', 'amount', 'penalty', 'status', 'actions'];

  ngOnInit() { 
    this.loadInvoices(); 
  }

 loadInvoices() {
  this.invoiceService.getAll().subscribe({
    next: (data: InvoiceResponse[]) => {
      this.invoices.set(data);
    },
    error: (err) => console.error('Error loading invoices:', err)
  });
}

  openForm(invoice?: InvoiceResponse) {
    const dialogRef = this.dialog.open(Invoice, { 
      width: '600px', 
      data: invoice,
      disableClose: true 
    });

    dialogRef.afterClosed().subscribe(result => { 
      if (result) this.loadInvoices(); 
    });
  }


}
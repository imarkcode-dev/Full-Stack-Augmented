import { Component, inject, OnInit, signal } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';

import { PaymentService } from '../../../core/services/payment.service';
import { PaymentResponse } from '../../../models/payment.model';
import { Payment } from '../payment/payment';
import { DatePipe, CurrencyPipe } from '@angular/common';
import { InvoiceService } from '../../../core/services/invoice.service';

@Component({
  selector: 'app-payment-list',
  imports: [MatTableModule, MatButtonModule, MatIconModule, MatDialogModule, DatePipe, CurrencyPipe],
  templateUrl: './payment-list.html',
  styleUrl: './payment-list.scss',
})
export class PaymentList implements OnInit {

  private paymentService = inject(PaymentService);
  private dialog = inject(MatDialog);

  payments = signal<PaymentResponse[]>([]);
  displayedColumns: string[] = ['invoice', 'date', 'amount', 'method', 'actions'];

  ngOnInit() { this.loadPayments(); }

  loadPayments() {
    this.paymentService.getAll().subscribe(res => this.payments.set(res));
  }

  openDialog(payment?: PaymentResponse) {
    const dialogRef = this.dialog.open(Payment, {
      width: '600px',
      data: payment || null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) this.loadPayments();
    });
  }




}

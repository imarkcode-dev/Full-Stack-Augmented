import { Component, inject, OnInit, signal, ChangeDetectorRef } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';
import { MatNativeDateModule, provideNativeDateAdapter } from '@angular/material/core';

import { PaymentService } from '../../../core/services/payment.service';
import { InvoiceService } from '../../../core/services/invoice.service';
import { PaymentResponse } from '../../../models/payment.model';
import { InvoiceResponse } from '../../../models/invoice.model';

@Component({
  selector: 'app-payment',
  providers: [provideNativeDateAdapter()],
  imports: [
    ReactiveFormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, 
    MatButtonModule, MatDatepickerModule, MatSelectModule, MatNativeDateModule
  ],
  templateUrl: './payment.html',
  styleUrl: './payment.scss',
})
export class Payment implements OnInit { 

  private fb = inject(NonNullableFormBuilder);
  private paymentService = inject(PaymentService);
  private invoiceService = inject(InvoiceService); // Para listar facturas
  private cdr = inject(ChangeDetectorRef);
  
  public dialogRef = inject(MatDialogRef<Payment>);
  public data = inject<PaymentResponse>(MAT_DIALOG_DATA);

  isEditMode = signal(false);
  isSaving = signal(false);
  invoices = signal<InvoiceResponse[]>([]);

  paymentForm = this.fb.group({
    invoiceId: [null as any, [Validators.required]],
    amountPaid: [0, [Validators.required, Validators.min(0.01)]],
    paymentDate: [new Date() as any, [Validators.required]],
    paymentMethod: ['CASH', [Validators.required]],
    referenceNumber: ['']
  });

  ngOnInit() {
    this.invoiceService.getAll().subscribe({
      next: (res) => {
        this.invoices.set(res);
        if (this.data) {
          this.isEditMode.set(true);
          this.initializeForm();
        }
      }
    });
  }

  private initializeForm() {
    setTimeout(() => {
      this.paymentForm.patchValue({
        invoiceId: this.data.invoiceId,
        amountPaid: this.data.amountPaid,
        paymentDate: new Date(this.data.paymentDate),
        paymentMethod: this.data.paymentMethod,
        referenceNumber: this.data.referenceNumber
      });
      this.cdr.detectChanges();
    }, 0);
  }

  compareInvoices(id1: any, id2: any): boolean {
    return id1 != null && id2 != null && String(id1) === String(id2);
  }

  onSave() {
    if (this.paymentForm.valid) {
      this.isSaving.set(true);
      const val = this.paymentForm.getRawValue();
      const payload = { ...val, paymentDate: new Date(val.paymentDate).toISOString() };

      const obs$ = this.isEditMode() 
        ? this.paymentService.update(this.data.id, payload)
        : this.paymentService.create(payload);

      obs$.subscribe({
        next: (res) => this.dialogRef.close(res),
        error: () => this.isSaving.set(false)
      });
    }
  }

  onCancel() { 
    this.dialogRef.close(); 
  }

}

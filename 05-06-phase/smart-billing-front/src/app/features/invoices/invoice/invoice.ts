import { Component, inject, OnInit, signal, ChangeDetectorRef } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';
import { MatNativeDateModule, provideNativeDateAdapter } from '@angular/material/core';

import { InvoiceService } from '../../../core/services/invoice.service';
import { ContractService } from '../../../core/services/contract.service'; 
import { InvoiceResponse } from '../../../models/invoice.model';
import { ContractResponse } from '../../../models/contract.model';

@Component({
  selector: 'app-invoice',
  standalone: true,
  providers: [provideNativeDateAdapter()],
  imports: [
    ReactiveFormsModule, 
    MatDialogModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule, 
    MatDatepickerModule,
    MatSelectModule,
    MatNativeDateModule
  ],
  templateUrl: './invoice.html',
  styleUrl: './invoice.scss',
})
export class Invoice implements OnInit {
  private fb = inject(NonNullableFormBuilder);
  private cdr = inject(ChangeDetectorRef);

  private invoiceService = inject(InvoiceService);
  private contractService = inject(ContractService);
  public dialogRef = inject(MatDialogRef<Invoice>);
  public data = inject<InvoiceResponse>(MAT_DIALOG_DATA);

  isEditMode = signal(false);
  isSaving = signal(false);
  contracts = signal<ContractResponse[]>([]);

  invoiceForm = this.fb.group({
    contractId: [null as any, [Validators.required]],
    invoiceNumber: ['', [Validators.required]],
    issueDate: [new Date() as any, [Validators.required]],
    dueDate: [new Date() as any, [Validators.required]],
    totalAmount: [0, [Validators.required, Validators.min(0.01)]],
    penaltyAmount: [0],
    status: ['PENDING', [Validators.required]]
  });


  ngOnInit() {
    
    this.contractService.getAll().subscribe({
      next: (res) => {
        this.contracts.set(res);
        if (this.data) {
          this.isEditMode.set(true);
          this.initializeForm(res);
        }
      },
      error: (err) => console.error('Error loading contracts', err)
    });
  }

  loadContracts() {
    this.contractService.getAll().subscribe({
      next: (res) => this.contracts.set(res),
      error: (err) => console.error('Error loading contracts', err)
    });
  }

  private initializeForm(allContracts: ContractResponse[]) {

    let targetId: number | null | undefined = this.data.contractId;

    if (!targetId && (this.data as any).contractTitle) {
      const found = allContracts.find(c => c.title === (this.data as any).contractTitle);
      targetId = found ? found.id : null;
    }

    setTimeout(() => {
      this.invoiceForm.patchValue({
        contractId: targetId,
        invoiceNumber: this.data.invoiceNumber,
        issueDate: this.data.issueDate ? new Date(this.data.issueDate) : new Date(),
        dueDate: this.data.dueDate ? new Date(this.data.dueDate) : new Date(),
        totalAmount: this.data.totalAmount,
        penaltyAmount: this.data.penaltyAmount ?? 0,
        status: this.data.status
      });
      this.cdr.detectChanges();
    }, 0);
  }

  compareContracts(id1: any, id2: any): boolean {
    if (id1 == null || id2 == null) return false;
    return String(id1) === String(id2);
  }

  onSave() {

    if (this.invoiceForm.valid) {
      this.isSaving.set(true);
      const val = this.invoiceForm.getRawValue();

      const payload: any = {
        ...val,
        issueDate: this.formatDate(val.issueDate),
        dueDate: this.formatDate(val.dueDate),

        ...(this.isEditMode() ? { id: this.data.id } : {})

      };
    
      const obs$ = this.isEditMode() 
        ? this.invoiceService.update(this.data.id, payload)
        : this.invoiceService.create(payload);

      console.log("obs: ");  
      console.log(obs$ );

      obs$.subscribe({
        next: (res) => {
          this.isSaving.set(false);
          this.dialogRef.close(res);
        },
        error: (err) => {
          this.isSaving.set(false);
          console.error('Error saving invoice', err);
        }
      });
    } else {
      this.invoiceForm.markAllAsTouched();
    }
    
  }

 private formatDate(date: any): string {
    if (!date) return '';
    const d = new Date(date);
  
    return d.toLocaleDateString('en-CA'); 
  }


  onCancel() {
    this.dialogRef.close();
  }
}
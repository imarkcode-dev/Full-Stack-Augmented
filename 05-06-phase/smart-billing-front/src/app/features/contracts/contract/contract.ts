

import { Component, inject, OnInit, signal } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select'; 
import { MatDatepickerModule } from '@angular/material/datepicker'; 
import { MatNativeDateModule } from '@angular/material/core';
import { provideNativeDateAdapter } from '@angular/material/core';

import { ContractService } from '../../../core/services/contract.service';
import { CustomerService } from '../../../core/services/customer.service';
import { ContractResponse } from '../../../models/contract.model';
import { CustomerResponse } from '../../../models/customer.model';

@Component({
  selector: 'app-contract',
  providers: [provideNativeDateAdapter()],
  imports: [
   ReactiveFormsModule, 
    MatDialogModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule,
    MatSelectModule,   
    MatDatepickerModule,  
    MatNativeDateModule
  ],
  templateUrl: './contract.html',
  styleUrl: './contract.scss',
})
export class Contract implements OnInit {

  private fb = inject(NonNullableFormBuilder);
  private contractService = inject(ContractService);
  private customerService = inject(CustomerService);
  public dialogRef = inject(MatDialogRef<Contract>); 
  public data = inject<ContractResponse>(MAT_DIALOG_DATA);

  isEditMode = signal(false);
  isSaving = signal(false);
  customers = signal<CustomerResponse[]>([]);

  contractForm = this.fb.group({
    customerId: [null as any, [Validators.required]],
    title: ['', [Validators.required]],
    startDate: ['', [Validators.required]],
    endDate: [''],
    monthlyFee: [0, [Validators.required, Validators.min(1)]],
    currency: ['USD', [Validators.required]],
    status: ['ACTIVE']
  });

  ngOnInit() {
    this.loadCustomers();
    if (this.data) {
      this.isEditMode.set(true);
      this.contractForm.patchValue(this.data);
    }
  }

  loadCustomers() {
    this.customerService.getAll().subscribe(res => this.customers.set(res));
  }

  onSave() {
    if (this.contractForm.valid) {
      this.isSaving.set(true);
      const val = this.contractForm.getRawValue();

      const payload: any = {
        ...val,
        startDate: this.formatDate(val.startDate),
        endDate: val.endDate ? this.formatDate(val.endDate) : null
      };
      
      const obs$ = this.isEditMode() 
        ? this.contractService.update(this.data.id!, payload) 
        : this.contractService.create(payload);

      obs$.subscribe({
        next: (res) => {
          this.isSaving.set(false);
          this.dialogRef.close(res);
        },
        error: (error) => {
          this.isSaving.set(false);
          console.error('Error al guardar contrato:', error);
        }
      });
    } else {
      this.contractForm.markAllAsTouched();
    }
  }

  
  onCancel() {
    this.dialogRef.close();
  }

  private formatDate(date: any): string {
    if (!date) return '';
    const d = new Date(date);
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const day = ('0' + d.getDate()).slice(-2);
    return `${d.getFullYear()}-${month}-${day}`;
  }
  
}

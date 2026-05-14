import { Component, inject, OnInit, signal } from '@angular/core';
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

  
  /*
  ngOnInit() {
    this.loadContracts();
    if (this.data) {
      this.isEditMode.set(true);

      this.invoiceForm.patchValue({
        ...this.data,
        contractId: this.data.contractId
      });

    }
  }
  */

   ngOnInit() {
    // 1. Cargamos los contratos
    this.contractService.getAll().subscribe({
      next: (res) => {
        this.contracts.set(res);
        
        // 2. Si estamos editando, disparamos la lógica de búsqueda y parcheo
        if (this.data) {
          this.isEditMode.set(true);
          this.findAndPatchContract(res);
        }
      },
      error: (err) => console.error('Error loading contracts', err)
    });
  }

  private findAndPatchContract(allContracts: ContractResponse[]) {
    // Intentamos obtener el ID directamente, si no existe, lo buscamos por título
    let targetId = this.data.contractId;

    if (!targetId && this.data.contractTitle) {
      const found = allContracts.find(c => c.title === this.data.contractTitle);
      targetId = found ? found.id : null;
    }

    // Parcheamos el formulario asegurando que el select tenga el ID correcto
    // Usamos setTimeout para asegurar que Angular terminó de renderizar las opciones del select
    setTimeout(() => {
      this.invoiceForm.patchValue({
        ...this.data,
        contractId: targetId,
        issueDate: new Date(this.data.issueDate),
        dueDate: new Date(this.data.dueDate)
      });
    }, 0);
  }


  loadContracts() {
    this.contractService.getAll().subscribe({
      next: (res) => this.contracts.set(res),
      error: (err) => console.error('Error loading contracts', err)
    });
  }

  compareContracts(id1: any, id2: any): boolean {
    if (id1 == null || id2 == null) return false;
    return String(id1) === String(id2);
  }


  onSave() {

    if (this.invoiceForm.valid) {
      this.isSaving.set(true);
      const val = this.invoiceForm.getRawValue();

      console.log("form: ");
      console.log(val);

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
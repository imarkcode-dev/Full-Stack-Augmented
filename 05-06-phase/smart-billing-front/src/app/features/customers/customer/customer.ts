
import { Component, inject, signal, OnInit } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CustomerRequest, CustomerResponse } from '../../../models/customer.model';
import { CustomerService } from '../../../core/services/customer.service';

@Component({
  selector: 'app-customer',
  imports: [
    ReactiveFormsModule, 
    MatDialogModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule],
  templateUrl: './customer.html',
  styleUrl: './customer.scss',
})
export class Customer implements OnInit {

  private fb = inject(NonNullableFormBuilder);
  private dialogRef = inject(MatDialogRef<Customer>);
  private customerService = inject(CustomerService);

  public data = inject<CustomerResponse>(MAT_DIALOG_DATA);


  isSaving = signal<boolean>(false);
  isEditMode = signal<boolean>(false);

  customerForm = this.fb.group({
    nameCustomer: ['', [Validators.required]],
    taxId: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.required]],
    address: ['', [Validators.required]]
  });

  ngOnInit(): void {
    if (this.data) {
      this.isEditMode.set(true);
      this.customerForm.patchValue(this.data);
    }
  }

 
  onSave() {
    if (this.customerForm.valid) {
      this.isSaving.set(true);
      const request = this.customerForm.getRawValue();

      const request$ = this.isEditMode() 
        ? this.customerService.update(this.data.id, request) 
        : this.customerService.create(request);

      request$.subscribe({
          next: (response) => {
            this.isSaving.set(false);
            this.dialogRef.close(response);
          },
          error: (err) => {
            this.isSaving.set(false);
            alert(err.error.message || 'Error processing request');
          }
        });
     }

  }

  onCancel() {
    this.dialogRef.close();
  }

}

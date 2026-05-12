
import { Component, inject, signal } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CustomerRequest } from '../../../models/customer.model';
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
export class Customer {

  private fb = inject(NonNullableFormBuilder);
  private dialogRef = inject(MatDialogRef<Customer>);
  private customerService = inject(CustomerService);


  isSaving = signal<boolean>(false);

  customerForm = this.fb.group({
    nameCustomer: ['', [Validators.required]],
    taxId: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.required]],
    address: ['', [Validators.required]]
  });

 
  onSave() {
    if (this.customerForm.valid) {
      this.isSaving.set(true);
      const request = this.customerForm.getRawValue();

      this.customerService.create(request).subscribe({
        next: (response) => {
          this.isSaving.set(false);
          this.dialogRef.close(response);
        },
        error: (err) => {
          this.isSaving.set(false);
          console.error('Error al guardar el cliente:', err);
          alert(err.error.message || 'Error al guardar el cliente');
        }
      });
    }
  }

  onCancel() {
    this.dialogRef.close();
  }

}

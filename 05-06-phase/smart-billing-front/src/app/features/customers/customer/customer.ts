
import { Component, inject } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CustomerRequest } from '../../../models/customer.model';

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

  customerForm = this.fb.group({
    nameCustomer: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.required]],
    address: ['', [Validators.required]]
  });

  onSave() {
    if (this.customerForm.valid) {
      this.dialogRef.close(this.customerForm.getRawValue());
    }
  }

  onCancel() {
    this.dialogRef.close();
  }

}

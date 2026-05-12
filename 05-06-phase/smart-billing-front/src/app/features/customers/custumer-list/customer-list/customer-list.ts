
import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';

import { CustomerService } from '../../../../core/services/customer.service';
import { CustomerResponse } from '../../../../models/customer.model';
import { Customer } from '../../customer/customer';

@Component({
  selector: 'app-customer-list',
  imports: [
    CommonModule, 
    MatTableModule, 
    MatButtonModule, 
    MatIconModule, 
    MatTooltipModule],
  templateUrl: './customer-list.html',
  styleUrl: './customer-list.scss',
})
export class CustomerList {

  private customerService = inject(CustomerService);

  private dialog = inject(MatDialog); 
  
  customers = signal<CustomerResponse[]>([]);
  displayedColumns: string[] = ['name', 'email', 'phone', 'status', 'actions'];

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers() {
    this.customerService.getAll().subscribe({
      next: (data) => this.customers.set(data),
      error: (err) => console.error('Error loading clients::', err)
    });
  }

  onAdd() {
  const dialogRef = this.dialog.open(Customer, {
    width: '500px',
    disableClose: true
  });

  dialogRef.afterClosed().subscribe((result) => {
    if (result) {
      this.loadCustomers(); 
    }
  });

}

  onEdit(customer: CustomerResponse) {
    const dialogRef = this.dialog.open(Customer, {
      width: '500px',
      disableClose: true,
      data: customer
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadCustomers();
      }
    });

 }

  onDelete(id: number) {
    if (confirm('Are you sure you want to delete this customer?')) {
      this.customerService.delete(id).subscribe(() => this.loadCustomers());
    }
  }

}

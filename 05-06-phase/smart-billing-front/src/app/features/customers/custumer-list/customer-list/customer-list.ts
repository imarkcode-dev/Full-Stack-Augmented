
import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { CustomerService } from '../../../../core/services/customer.service';
import { CustomerResponse } from '../../../../models/customer.model';

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
  
  customers = signal<CustomerResponse[]>([]);
  displayedColumns: string[] = ['name', 'email', 'phone', 'status', 'actions'];

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers() {
    this.customerService.getAll().subscribe(data => this.customers.set(data));
  }

  onAdd() {
    console.log('Open Dialog for New Customer');
  }

  onEdit(customer: CustomerResponse) {
    console.log('Edit customer:', customer.id);
  }

  onDelete(id: number) {
    if (confirm('Are you sure you want to delete this customer?')) {
      this.customerService.delete(id).subscribe(() => this.loadCustomers());
    }
  }

}

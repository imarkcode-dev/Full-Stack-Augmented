import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmployeeService } from '../services/employee.service';
import { EmployeeDTO } from '../models/employee.dto';
import { EmployeeNewEdit } from '../employee-new-edit/employee-new-edit';
import { AuthService } from '../../../core/services/auth/auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-employee',
  imports: [CommonModule, EmployeeNewEdit],
  templateUrl: './employee.html',
  styleUrl: './employee.css',
})
export class Employee implements OnInit {

  private empService = inject(EmployeeService);
  private authService = inject(AuthService);
  private router = inject(Router);
  
  
  employees = signal<EmployeeDTO[]>([]);
  selectedEmployee = signal<EmployeeDTO | null>(null);
  showForm = signal<boolean>(false);

  ngOnInit() {
     this.load(); 
  }
  

  load() { 
    this.empService.getAll().subscribe(data => this.employees.set(data)); 
  }


  onDelete(id: number) {
    if(confirm('Delete employee?')) {
      this.empService.delete(id).subscribe(() => this.load());
    }
  }


  openNew() {
    this.selectedEmployee.set(null);
    this.showForm.set(true);
  }

  openEdit(employee: EmployeeDTO) {
    this.selectedEmployee.set(employee);
    this.showForm.set(true);
  }

  handleFinished() {
    this.showForm.set(false);
    this.load();
  }

  onLogout() {
    this.authService.logout(); 
    this.router.navigate(['/login']); 
  }

}

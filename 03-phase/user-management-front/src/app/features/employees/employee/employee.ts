import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmployeeService } from '../services/employee.service';
import { EmployeeDTO } from '../models/employee.dto';
import { EmployeeNewEdit } from '../employee-new-edit/employee-new-edit';
import { AuthService } from '../../../core/services/auth/auth.service';
import { Router } from '@angular/router';


/**
 * Employee component manages the display and operations for employee records,
 * including listing, creating, editing, and deleting employees.
 */
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

  /**
   * Initializes the component by loading the list of employees.
   */
  ngOnInit() {
     this.load(); 
  }
  

  /**
   * Loads all employees from the service and updates the employees signal.
   */
  load() { 
    this.empService.getAll().subscribe(data => this.employees.set(data)); 
  }


  /**
   * Deletes an employee by ID after user confirmation and reloads the list.
   * @param id The ID of the employee to delete.
   */
  onDelete(id: number) {
    if(confirm('Delete employee?')) {
      this.empService.delete(id).subscribe(() => this.load());
    }
  }


  /**
   * Opens the form for creating a new employee.
   */
  openNew() {
    this.selectedEmployee.set(null);
    this.showForm.set(true);
  }

  /**
   * Opens the form for editing the specified employee.
   * @param employee The employee to edit.
   */
  openEdit(employee: EmployeeDTO) {
    this.selectedEmployee.set(employee);
    this.showForm.set(true);
  }

  /**
   * Handles the completion of form operations by hiding the form and reloading employees.
   */
  handleFinished() {
    this.showForm.set(false);
    this.load();
  }

  /**
   * Logs out the current user and navigates to the login page.
   */
  onLogout() {
    this.authService.logout(); 
    this.router.navigate(['/login']); 
  }

}

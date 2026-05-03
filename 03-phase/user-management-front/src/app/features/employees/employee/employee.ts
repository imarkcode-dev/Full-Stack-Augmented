import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { EmployeeService } from '../services/employee.service';
import { EmployeeDTO } from '../models/employee.dto';


@Component({
  selector: 'app-employee',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './employee.html',
  styleUrl: './employee.css',
})
export class Employee implements OnInit {

  private empService = inject(EmployeeService);
  private fb = inject(FormBuilder);
  
  employees = signal<EmployeeDTO[]>([]);
  employeeForm: FormGroup;
  isEditing = signal<boolean>(false);
  currentId = signal<number | null>(null);

  constructor() {
    this.employeeForm = this.fb.group({
      name: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit() {
     this.load(); 
  }

  load() { 
    this.empService.getAll().subscribe(data => this.employees.set(data)); 
  }

  onDelete(id: number) {
    this.empService.delete(id).subscribe(() => this.load());
  }


  // Al hacer clic en Editar
  onEdit(employee: EmployeeDTO) {
    this.isEditing.set(true);
    this.currentId.set(employee.id!);
    this.employeeForm.patchValue(employee); // Carga los datos en los inputs
  }

  onSave() {
    if (this.employeeForm.invalid) return;

    const employeeData: EmployeeDTO = this.employeeForm.value;

    if (this.isEditing()) {
      // UPDATE
      this.empService.update(this.currentId()!, employeeData).subscribe(() => {
        this.resetForm();
        this.load();
      });
    } else {
      // SAVE 
      this.empService.save(employeeData).subscribe(() => {
        this.resetForm();
        this.load();
      });
    }
  }

  resetForm() {
    this.isEditing.set(false);
    this.currentId.set(null);
    this.employeeForm.reset();
  }

}

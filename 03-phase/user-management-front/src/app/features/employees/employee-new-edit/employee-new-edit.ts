import { Component, inject, input, output, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EmployeeService } from '../services/employee.service';
import { EmployeeDTO } from '../models/employee.dto';

/**
 * EmployeeNewEdit component provides a form for creating new employees or editing existing ones.
 * It uses reactive forms for validation and handles save/cancel operations.
 */
@Component({
  selector: 'app-employee-new-edit',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './employee-new-edit.html',
  styleUrl: './employee-new-edit.css',
})
export class EmployeeNewEdit {

  private fb = inject(FormBuilder);
  private empService = inject(EmployeeService);

  employeeToEdit = input<EmployeeDTO | null>(null);
  onFinished = output<void>();

  employeeForm: FormGroup;

  constructor() {

    this.employeeForm = this.fb.group({
      name: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.pattern('^[0-9-]+$')]]
    });

    effect(() => {
      const emp = this.employeeToEdit();
      if (emp) {
        this.employeeForm.patchValue(emp);
      } else {
        this.employeeForm.reset();
      }
    });

  }

  /**
   * Saves the employee data by either creating a new employee or updating an existing one,
   * then resets the form and emits the finished event.
   */
  onSave() {
    if (this.employeeForm.invalid) return;
    const data = this.employeeForm.value;
    const empId = this.employeeToEdit()?.id;

    const request = empId 
      ? this.empService.update(empId, data) 
      : this.empService.save(data);

    request.subscribe(() => {
      this.employeeForm.reset();
      this.onFinished.emit();
    });
  }

  /**
   * Cancels the form operation by resetting the form and emitting the finished event.
   */
  cancel() {
    this.employeeForm.reset();
    this.onFinished.emit();
  }

}

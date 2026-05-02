import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmployeeService } from '../services/employee.service';
import { EmployeeDTO } from '../models/employee.dto';


@Component({
  selector: 'app-employee',
  imports: [CommonModule],
  templateUrl: './employee.html',
  styleUrl: './employee.css',
})
export class Employee implements OnInit {

  private empService = inject(EmployeeService);
  
  employees = signal<EmployeeDTO[]>([]);

  ngOnInit() { this.load(); }

  load() { this.empService.getAll().subscribe(data => this.employees.set(data)); }

  onDelete(id: number) {
    this.empService.delete(id).subscribe(() => this.load());
  }
}

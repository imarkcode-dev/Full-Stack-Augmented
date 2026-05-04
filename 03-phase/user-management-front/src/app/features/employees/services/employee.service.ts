import { inject, Injectable  } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmployeeDTO } from '../models/employee.dto';

/**
 * Service for performing CRUD operations on employee data via HTTP requests.
 */
@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  private http = inject(HttpClient);
  private url = 'http://localhost:8181/api/v1/employees';

  /**
   * Retrieves all employees from the API.
   * @returns Observable of an array of EmployeeDTO.
   */
  getAll() { 
    return this.http.get<EmployeeDTO[]>(this.url); 
  }
  
  /**
   * Retrieves a specific employee by ID from the API.
   * @param id The ID of the employee to retrieve.
   * @returns Observable of EmployeeDTO.
   */
  getById(id: number) { 
    return this.http.get<EmployeeDTO>(`${this.url}/${id}`); 
  }
  
  /**
   * Saves a new employee to the API.
   * @param e The employee data to save.
   * @returns Observable of the created EmployeeDTO.
   */
  save(e: EmployeeDTO) { 
    return this.http.post<EmployeeDTO>(this.url, e); 
  }
  
  /**
   * Deletes an employee by ID from the API.
   * @param id The ID of the employee to delete.
   * @returns Observable of void.
   */
  delete(id: number) { 
    return this.http.delete(`${this.url}/${id}`); 
  }

  /**
   * Updates an existing employee by ID in the API.
   * @param id The ID of the employee to update.
   * @param employee The updated employee data.
   * @returns Observable of the updated EmployeeDTO.
   */
  update(id: number, employee: EmployeeDTO): Observable<EmployeeDTO> {
    return this.http.put<EmployeeDTO>(`${this.url}/${id}`, employee);
  }

}

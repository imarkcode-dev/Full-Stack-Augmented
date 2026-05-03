import { inject, Injectable  } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmployeeDTO } from '../models/employee.dto';

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  private http = inject(HttpClient);
  private url = 'http://localhost:8181/api/v1/employees';

  getAll() { 
    return this.http.get<EmployeeDTO[]>(this.url); 
  }
  
  getById(id: number) { 
    return this.http.get<EmployeeDTO>(`${this.url}/${id}`); 
  }
  
  save(e: EmployeeDTO) { 
    return this.http.post<EmployeeDTO>(this.url, e); 
  }
  
  delete(id: number) { 
    return this.http.delete(`${this.url}/${id}`); 
  }

  update(id: number, employee: EmployeeDTO): Observable<EmployeeDTO> {
    return this.http.put<EmployeeDTO>(`${this.url}/${id}`, employee);
  }

}

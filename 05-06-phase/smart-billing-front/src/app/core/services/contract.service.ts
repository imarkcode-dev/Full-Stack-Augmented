import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { ContractRequest, ContractResponse } from '../../models/contract.model';


@Injectable({
  providedIn: 'root',
})
export class ContractService {

  private http = inject(HttpClient);
  
  private apiUrl = `${environment.apiContractUrl}`;

  getAll(): Observable<ContractResponse[]> {
    return this.http.get<ContractResponse[]>(this.apiUrl);
  }


  getById(id: number): Observable<ContractResponse> {
    return this.http.get<ContractResponse>(`${this.apiUrl}/${id}`);
  }

  
  create(contract: ContractRequest): Observable<ContractResponse> {
    return this.http.post<ContractResponse>(this.apiUrl, contract);
  }

  
  update(id: number, contract: ContractRequest): Observable<ContractResponse> {
    return this.http.put<ContractResponse>(`${this.apiUrl}/${id}`, contract);
  }

  
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getByCustomerId(customerId: number): Observable<ContractResponse[]> {
    return this.http.get<ContractResponse[]>(`${this.apiUrl}/customer/${customerId}`);
  }
  
}

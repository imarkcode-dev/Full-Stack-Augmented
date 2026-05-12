import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { CustomerResponse, CustomerRequest } from '../../models/customer.model';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {

  private http = inject(HttpClient);
  private apiUrl = environment.apiCustomerUrl;

  getAll() {
    return this.http.get<CustomerResponse[]>(this.apiUrl);
  }

  create(customer: CustomerRequest) {
    return this.http.post<CustomerResponse>(this.apiUrl, customer, {
      observe: 'body',
      responseType: 'json'
    });
  }

  update(id: number, customer: CustomerRequest) {
    return this.http.put<CustomerResponse>(`${this.apiUrl}/${id}`, customer);
  }

  delete(id: number) {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}

import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentResponse, PaymentRequest } from '../../models/payment.model';
import { environment } from '../../../environments/environment';


@Injectable({
  providedIn: 'root',
})
export class PaymentService {

  private http = inject(HttpClient);
  private apiUrl = `${environment.apiPayment}`;

  getAll(): Observable<PaymentResponse[]> {
    return this.http.get<PaymentResponse[]>(this.apiUrl);
  }

  create(payment: PaymentRequest): Observable<PaymentResponse> {
    return this.http.post<PaymentResponse>(this.apiUrl, payment);
  }

  update(id: number, payment: PaymentRequest): Observable<PaymentResponse> {
    return this.http.put<PaymentResponse>(`${this.apiUrl}/${id}`, payment);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}

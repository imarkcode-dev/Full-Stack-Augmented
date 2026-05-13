import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InvoiceResponse, InvoiceRequest } from '../../models/invoice.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class InvoiceService {

  private http = inject(HttpClient);
  private apiUrl = `${environment.apiInvoiceUrl}`;

  getAll(): Observable<InvoiceResponse[]> {
    return this.http.get<InvoiceResponse[]>(this.apiUrl);
  }

  create(invoice: InvoiceRequest): Observable<InvoiceResponse> {
    return this.http.post<InvoiceResponse>(this.apiUrl, invoice);
  }

  update(id: number, invoice: InvoiceRequest): Observable<InvoiceResponse> {
    return this.http.put<InvoiceResponse>(`${this.apiUrl}/${id}`, invoice);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}

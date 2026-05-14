import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DashboardResponse } from '../../models/dashboard.model';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {

  private http = inject(HttpClient);
  private apiUrl = `${environment.apiDashboardUrl}`;


  /**
  * Obtains consolidated financial metrics (Total Invoiced, 
  * Total Collected, Overdue Amount) and historical cash flow.
  */
  getSummary(): Observable<DashboardResponse> {
    return this.http.get<DashboardResponse>(`${this.apiUrl}/summary`);
  }

}

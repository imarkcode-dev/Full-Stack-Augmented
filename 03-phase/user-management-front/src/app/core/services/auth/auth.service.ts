import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginRequest, AuthResponse } from '../../../features/auth/models/auth.dto';
import { tap } from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8181/api/v1/auth';
  isAuthenticated = signal<boolean>(!!localStorage.getItem('token'));
 

  login(credentials: LoginRequest) {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap(res => {
          console.log(res);
          localStorage.setItem('token', res.token);
          this.isAuthenticated.set(true);
        })
    );

  }

  logout() {
    localStorage.removeItem('token');
    this.isAuthenticated.set(false);
  }

  getToken() { return localStorage.getItem('token'); }

}

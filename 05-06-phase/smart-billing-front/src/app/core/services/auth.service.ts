import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { tap } from 'rxjs';

import { LoginRequest, AuthResponse } from '../../models/auth.dto';
import { environment } from '../../../environments/environment';


/**
 * Service for managing user authentication.
 * Handles login, logout, and token management.
 */
@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private readonly headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

  private http = inject(HttpClient);
  private apiUrl = environment.apiAuthUrl;
  isAuthenticated = signal<boolean>(!!localStorage.getItem('token'));

  /**
   * Authenticates a user with the provided credentials.
   * Stores the token in localStorage and updates authentication state on success.
   * @param credentials - The login credentials (username and password)
   * @returns Observable with the authentication response
   */
  login(credentials: LoginRequest) {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials, { headers: this.headers })
      .pipe(
        tap(res => {
          localStorage.setItem('token', res.token);
          this.isAuthenticated.set(true);
        })
    );

  }

  /**
   * Logs out the current user.
   * Removes the token from localStorage and updates authentication state.
   */
  logout() {
    localStorage.removeItem('token');
    this.isAuthenticated.set(false);
  }

  /**
   * Retrieves the stored authentication token.
   * @returns The token string or null if no token is stored
   */
  getToken() { return localStorage.getItem('token'); }

}
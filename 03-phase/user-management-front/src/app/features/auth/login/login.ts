import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth/auth.service';


/**
 * Login component handles user authentication, including login and logout functionality.
 * It manages the login form data and authentication state.
 */
@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  private auth = inject(AuthService);
  private router = inject(Router);
  loginData = { username: '', password: '' };
  isAuthenticated = signal<boolean>(!!localStorage.getItem('token'));

  /**
   * Handles the login process by calling the auth service with the provided credentials
   * and navigates to the employees page upon successful authentication.
   */
  onLogin() {
    this.auth.login(this.loginData).subscribe(() => this.router.navigate(['/employees']));
  }

  /**
   * Logs out the user by removing the authentication token from local storage
   * and updating the authentication state signal.
   */
  logout(): void {
    localStorage.removeItem('token');
    this.isAuthenticated.set(false);
  }

}

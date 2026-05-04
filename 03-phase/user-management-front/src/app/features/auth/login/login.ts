import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth/auth.service';


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

  onLogin() {
    this.auth.login(this.loginData).subscribe(() => this.router.navigate(['/employees']));
  }

  logout(): void {
    localStorage.removeItem('token');
    this.isAuthenticated.set(false);
  }

}

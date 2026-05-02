import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./features/auth/login/login').then(m => m.Login) },
  { path: 'employees', canActivate: [authGuard], loadComponent: () => import('./features/employees/employee/employee').then(m => m.Employee) },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];

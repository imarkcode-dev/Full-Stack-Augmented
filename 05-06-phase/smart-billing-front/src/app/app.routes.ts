import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { 
    path: 'login', 
    loadComponent: () => import('./features/login/login').then(m => m.Login) 
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./shared/layout/layout/layout').then(m => m.Layout),
    children: [
      { 
        path: 'dashboard', 
        loadComponent: () => import('./features/dashboard/dashboard').then(m => m.Dashboard) 
      },
      { 
        path: 'customers', 
        loadComponent: () => 
            import('./features/customers/custumer-list/customer-list/customer-list').then(m => m.CustomerList) 
      },
      { 
        path: 'customers/new', 
        loadComponent: () => 
          import('./features/customers/customer/customer').then(m => m.Customer) 
      },
      { 
        path: 'customers/edit/:id', 
        loadComponent: () => 
          import('./features/customers/customer/customer').then(m => m.Customer) 
      },
      { 
        path: 'contracts', 
        loadComponent: () => 
          import('./features/contracts/contract-list/contract-list').then(m => m.ContractList) 
      },
       { 
        path: 'contracts/new', 
        loadComponent: () => 
          import('./features/customers/customer/customer').then(m => m.Customer) 
      },
      { 
        path: 'contracts/edit/:id', 
        loadComponent: () => 
          import('./features/customers/customer/customer').then(m => m.Customer) 
      },
      {
        path: 'invoices',
        loadComponent: () => 
          import('./features/invoices/invoice-list/invoice-list').then(m => m.InvoiceList)
      },
      {
        path: 'invoices/new',
        loadComponent: () => 
          import('./features/invoices/invoice/invoice').then(m => m.Invoice)
      },
      {
        path: 'invoices/edit/:id',
        loadComponent: () => 
          import('./features/invoices/invoice/invoice').then(m => m.Invoice)
      },
      {
        path: 'payments',
        loadComponent: () => 
          import('./features/payments/payment-list/payment-list').then(m => m.PaymentList)
      },
      {
        path: 'payments/new',
        loadComponent: () => 
          import('./features/payments/payment/payment').then(m => m.Payment)
      },
      {
        path: 'payments/edit/:id',
        loadComponent: () => 
          import('./features/payments/payment/payment').then(m => m.Payment)
      },

      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }

    ]
  },

  { path: '**', redirectTo: 'login' }
];




import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../../core/services/auth.service';



@Component({
  selector: 'app-layout',
  imports: [
    RouterOutlet, 
    RouterLink, 
    RouterLinkActive, 
    MatSidenavModule, 
    MatListModule, 
    MatToolbarModule, 
    MatIconModule, 
    MatButtonModule],
  templateUrl: './layout.html',
  styleUrl: './layout.scss',
})
export class Layout {

  private authService = inject(AuthService);

  logout() { 
    this.authService.logout(); 
  }

}

import { Component, OnInit } from '@angular/core';
import { ThemeToggleComponent } from './theme-toggle/theme-toggle.component';
import { RouterLink, RouterOutlet, Router } from '@angular/router';
import { CommonModule } from '@angular/common'; // ✅ Добавляем CommonModule
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [
    CommonModule,         // ✅ ОБЯЗАТЕЛЬНО чтобы заработал *ngIf
    ThemeToggleComponent,
    RouterOutlet,
    RouterLink
  ]
})
export class AppComponent implements OnInit {

  constructor(public authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.loadTheme();
  }

  toggleTheme(): void {
    const body = document.body;
    body.classList.toggle('light-theme');

    const isLight = body.classList.contains('light-theme');
    localStorage.setItem('theme', isLight ? 'light' : 'dark');
  }

  loadTheme(): void {
    const theme = localStorage.getItem('theme');
    if (theme === 'light') {
      document.body.classList.add('light-theme');
    } else {
      document.body.classList.remove('light-theme');
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

import { Component, OnInit } from '@angular/core';
import {ThemeToggleComponent} from './theme-toggle/theme-toggle.component';
import {RouterLink, RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [
    ThemeToggleComponent,
    RouterOutlet,
    RouterLink
  ]
})
export class AppComponent implements OnInit {

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
}

import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-theme-toggle',
  templateUrl: './theme-toggle.component.html',
  styleUrls: ['./theme-toggle.component.css']
})
export class ThemeToggleComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    this.applyTheme();
  }

  getTheme(): string {
    return localStorage.getItem('theme') || 'light';
  }

  applyTheme(theme: string = this.getTheme()): void {
    document.body.classList.toggle('dark', theme === 'dark');
  }

  toggleTheme(): void {
    const nextTheme = this.getTheme() === 'dark' ? 'light' : 'dark';
    localStorage.setItem('theme', nextTheme);
    this.applyTheme(nextTheme);
  }
}

// src/app/auth/auth.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {AuthService} from '../services/auth.service';  // Импортируем AuthService

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {
  isLoginMode = true;

  form = {
    username: '',
    email: '',
    password: ''
  };

  constructor(
    private authService: AuthService,  // Теперь доступен AuthService
    private router: Router
  ) {}

  toggleMode(): void {
    this.isLoginMode = !this.isLoginMode;
  }

  submit(): void {
    if (this.isLoginMode) {
      // Вход
      this.authService.login({
        username: this.form.username,
        password: this.form.password
      }).subscribe({
        next: () => {
          alert('Login successful!');
          window.location.href = '/';  // Редирект на главную после логина
        },
        error: err => {
          alert(err.error?.message || 'Login failed.');
        }
      });
    } else {
      // Регистрация
      this.authService.register(this.form).subscribe({
        next: () => {
          alert('Registration successful! Please log in.');
          this.isLoginMode = true;  // Переключаем на форму логина
          this.form.password = '';  // Очищаем поле пароля
        },
        error: err => {
          alert(err.error?.message || 'Registration failed.');
        }
      });
    }
  }
}

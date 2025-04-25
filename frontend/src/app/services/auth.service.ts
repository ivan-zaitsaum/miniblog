// src/app/auth/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly API = 'http://localhost:8080/api/auth';  // URL API
  private tokenKey = 'auth_token';  // Ключ для хранения токена в localStorage

  constructor(private http: HttpClient) {}

  // Логин
  login(credentials: { username: string; password: string }) {
    return this.http.post<{ token: string }>(`${this.API}/login`, credentials)
      .pipe(tap(res => localStorage.setItem(this.tokenKey, res.token)));  // Сохраняем токен
  }

  // Регистрация
  register(data: { username: string; email: string; password: string }) {
    return this.http.post(`${this.API}/register`, data);
  }

  // Получение токена из localStorage
  // src/app/auth/auth.service.ts
  getToken(): string | null {
    return localStorage.getItem('auth_token');
  }


  // Проверка, авторизован ли пользователь
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  // Выход
  logout(): void {
    localStorage.removeItem(this.tokenKey);  // Удаляем токен
  }

  getUsername(): string | null {
    const token = localStorage.getItem('auth_token');
    if (!token) return null;

    // Расшифровываем payload токена
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub || null;  // если имя пользователя хранится в sub
  }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  imports: [CommonModule, RouterLink]
})
export class ProfileComponent implements OnInit {
  user: any = null;
  posts: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    // ✅ Добавляем сюда проверку токена
    const token = localStorage.getItem('auth_token');
    console.log('Текущий токен в ProfileComponent:', token);

    this.loadProfile();
    this.loadUserPosts();
  }

  loadProfile(): void {
    this.http.get<any>('http://localhost:8080/api/users/me').subscribe({
      next: (profile) => {
        this.user = profile;
      },
      error: (err) => {
        console.error('Ошибка при загрузке профиля:', err);
      }
    });
  }

  loadUserPosts(): void {
    this.http.get<any[]>('http://localhost:8080/api/users/posts').subscribe({
      next: (posts) => {
        this.posts = posts;
      },
      error: (err) => {
        console.error('Ошибка при загрузке постов пользователя:', err);
      }
    });
  }
}

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
  avatarUrl: string = 'assets/default-avatar.png'; // Стартуем с дефолта

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadProfile();
    this.loadUserPosts();
  }

  loadProfile(): void {
    this.http.get<any>('http://localhost:8080/api/users/me').subscribe({
      next: (profile) => {
        this.user = profile;
        if (this.user.avatar) {
          this.avatarUrl = 'http://localhost:8080/uploads/' + this.user.avatar;
        }
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

  selectedFile: File | null = null;

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  uploadAvatar(): void {
    if (!this.selectedFile) {
      alert('Please select a file first!');
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile);

    const token = localStorage.getItem('auth_token');

    this.http.post<any>('http://localhost:8080/api/users/avatar', formData, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: (response) => {
        console.log('Аватар загружен!', response);
        this.loadProfile(); // Перезагружаем профиль чтобы обновить аватар
      },
      error: (err) => {
        console.error('Ошибка при загрузке аватара:', err);
      }
    });
  }
}

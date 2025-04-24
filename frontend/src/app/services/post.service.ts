// src/app/posts/post.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Post {
  id?: number;
  title: string;
  content: string;
  createdAt?: string;
  authorUsername?: string;
}

@Injectable({ providedIn: 'root' })
export class PostService {
  private readonly API = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Post[]> {
    return this.http.get<Post[]>(this.API);
  }

  create(post: Post): Observable<Post> {
    const token = localStorage.getItem('auth_token');  // Получаем токен
    const headers = token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : {};  // Добавляем токен в заголовок

    return this.http.post<Post>(this.API, post, { headers });  // Отправляем с заголовками
  }
}

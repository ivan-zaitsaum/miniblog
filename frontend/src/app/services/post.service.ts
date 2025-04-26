import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Post {
  id?: number;
  title: string;
  content: string;
  createdAt?: string;
  authorUsername?: string;
  categoryNames?: string[];
  tagNames?: string[];
}

@Injectable({ providedIn: 'root' })
export class PostService {
  private readonly API = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Post[]> {
    return this.http.get<Post[]>(this.API);
  }

  create(post: Post): Observable<Post> {
    const token = localStorage.getItem('auth_token');
    const headers = token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : {};
    return this.http.post<Post>(this.API, post, { headers });
  }

  deletePost(id: number): Observable<void> {
    const token = localStorage.getItem('auth_token');
    const headers = token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : {};
    return this.http.delete<void>(`${this.API}/${id}`, { headers });
  }

  updatePost(id: number, post: Post): Observable<void> {
    const token = localStorage.getItem('auth_token');
    const headers = token
      ? new HttpHeaders().set('Authorization', `Bearer ${token}`)
      : new HttpHeaders();
    return this.http.put<void>(`${this.API}/${id}`, post, { headers });
  }

  // 🔥 Новый метод для фильтрации постов
  getFiltered(query: string, categoryId: number | null, tagId: number | null): Observable<Post[]> {
    let params = new HttpParams();

    if (query) {
      params = params.set('q', query);
    }
    if (categoryId !== null) {
      params = params.set('categoryId', categoryId.toString());
    }
    if (tagId !== null) {
      params = params.set('tagId', tagId.toString());
    }

    return this.http.get<Post[]>(this.API, { params });
  }
}

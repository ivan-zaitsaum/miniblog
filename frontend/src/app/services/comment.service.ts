import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private readonly API = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) {}

  getComments(postId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.API}/${postId}/comments`);
  }

  addComment(postId: number, content: string): Observable<void> {
    const token = localStorage.getItem('auth_token');
    const headers = token
      ? new HttpHeaders().set('Authorization', `Bearer ${token}`)
      : new HttpHeaders();

    return this.http.post<void>(`${this.API}/${postId}/comments`, { content }, { headers });
  }
}

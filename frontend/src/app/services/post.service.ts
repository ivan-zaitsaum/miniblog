import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Post {
  id?: number;
  title: string;
  content: string;
  authorUsername: string;
  createdAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private apiUrl = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) { }

  getAll(): Observable<Post[]> {
    return this.http.get<Post[]>(this.apiUrl);
  }

  create(post: Post): Observable<Post> {
    return this.http.post<Post>(this.apiUrl, post);
  }
}

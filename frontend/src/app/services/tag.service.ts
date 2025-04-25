import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tag } from '../models/tag.model';

@Injectable({ providedIn: 'root' })
export class TagService {
  private readonly API = 'http://localhost:8080/api/tags';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Tag[]> {
    const token = localStorage.getItem('auth_token');
    const headers = token
      ? new HttpHeaders().set('Authorization', `Bearer ${token}`)
      : new HttpHeaders();

    return this.http.get<Tag[]>(this.API, { headers });
  }


  create(name: string): Observable<Tag> {
    const token = localStorage.getItem('auth_token');
    const headers = token
      ? new HttpHeaders().set('Authorization', `Bearer ${token}`)
      : new HttpHeaders();

    return this.http.post<Tag>(this.API, { name }, { headers });
  }



}

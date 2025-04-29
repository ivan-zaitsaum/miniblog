import { Component, OnInit } from '@angular/core';
import { CommonModule }  from '@angular/common';
import { FormsModule }   from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router, RouterLink }      from '@angular/router';


@Component({
  selector: 'app-profile',
  standalone: true,
  templateUrl: './profile.component.html',
  styleUrls:   ['./profile.component.css'],
  imports: [CommonModule, RouterLink, FormsModule]
})
export class ProfileComponent implements OnInit {
  user: any = null;
  posts: any[] = [];
  avatarUrl: string = 'assets/default-avatar.png';
  selectedFile: File | null = null;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProfile();
    this.loadUserPosts();
  }

  loadProfile(): void {
    this.http.get<any>('http://localhost:8080/api/users/me').subscribe({
      next: profile => {
        this.user = profile;
        if (this.user.avatar) {
          this.avatarUrl = 'http://localhost:8080/uploads/' + this.user.avatar;
        }
      },
      error: err => console.error('Ошибка при загрузке профиля:', err)
    });
  }

  loadUserPosts(): void {
    this.http.get<any[]>('http://localhost:8080/api/users/posts').subscribe({
      next: posts => this.posts = posts,
      error: err   => console.error('Ошибка при загрузке постов пользователя:', err)
    });
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0] || null;
  }

  uploadAvatar(): void {
    if (!this.selectedFile) {
      alert('Please select a file first!');
      return;
    }
    const formData = new FormData();
    formData.append('file', this.selectedFile);
    const token = localStorage.getItem('auth_token') || '';
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    this.http.post<any>('http://localhost:8080/api/users/avatar', formData, { headers })
      .subscribe({
        next: ()       => this.loadProfile(),
        error: err     => console.error('Ошибка при загрузке аватара:', err)
      });
  }

  // ——————— Добавляем два метода:

  deletePost(id: number): void {
    if (!confirm('Are you sure you want to delete this post?')) return;
    const token = localStorage.getItem('auth_token') || '';
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    this.http.delete(`http://localhost:8080/api/posts/${id}`, { headers })
      .subscribe({
        next: ()       => this.loadUserPosts(),
        error: err     => console.error('Ошибка при удалении поста:', err)
      });
  }

  editPost(id: number): void {
    // Перенаправляем на страницу редактирования точно так же, как в PostsComponent
    this.router.navigate(['/edit-post', id]);
  }
}

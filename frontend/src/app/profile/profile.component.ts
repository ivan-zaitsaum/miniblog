import { Component, OnInit }        from '@angular/core';
import { CommonModule }             from '@angular/common';
import { FormsModule }              from '@angular/forms';
import { HttpClient, HttpHeaders }  from '@angular/common/http';
import { Router, RouterLink }       from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: any = {};
  posts: any[] = [];
  avatarUrl = 'assets/default-avatar.png';
  selectedFile: File | null = null;

  // для inline-редактирования username
  isEditingUsername = false;
  newUsername = '';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProfile();
    this.loadUserPosts();
  }

  private getAuthHeaders(json = false): HttpHeaders {
    let h = new HttpHeaders().set(
      'Authorization',
      `Bearer ${localStorage.getItem('auth_token') || ''}`
    );
    if (json) h = h.set('Content-Type', 'application/json');
    return h;
  }

  loadProfile(): void {
    this.http
      .get<any>('http://localhost:8080/api/users/me', { headers: this.getAuthHeaders() })
      .subscribe({
        next: p => {
          this.user = p;
          this.avatarUrl = p.avatar
            ? `http://localhost:8080/uploads/${p.avatar}`
            : 'assets/default-avatar.png';
        },
        error: e => this.handleAuthError(e)
      });
  }

  loadUserPosts(): void {
    this.http
      .get<any[]>('http://localhost:8080/api/users/posts', { headers: this.getAuthHeaders() })
      .subscribe({
        next: posts => (this.posts = posts),
        error: e => this.handleAuthError(e)
      });
  }

  onFileSelected(e: any): void {
    this.selectedFile = e.target.files[0] || null;
  }

  uploadAvatar(): void {
    if (!this.selectedFile) return alert('Please select a file first!');
    const fd = new FormData();
    fd.append('file', this.selectedFile);
    this.http
      .post<any>('http://localhost:8080/api/users/avatar', fd, { headers: this.getAuthHeaders() })
      .subscribe({
        next: () => {
          this.selectedFile = null;
          this.loadProfile();
        },
        error: e => console.error(e)
      });
  }

  startEdit(): void {
    this.newUsername = this.user.username;
    this.isEditingUsername = true;
  }

  saveUsername(): void {
    if (!this.newUsername.trim()) return;
    this.http
      .put<any>(
        'http://localhost:8080/api/users/me',
        { username: this.newUsername },
        { headers: this.getAuthHeaders(true) }
      )
      .subscribe({
        next: () => {
          // выкидываем на /auth
          localStorage.removeItem('auth_token');
          alert('Имя изменено, войдите под новым именем');
          this.router.navigate(['/auth']);
        },
        error: e => {
          if (e.status === 409) alert('Имя занято');
          else this.handleAuthError(e);
        }
      });
  }

  cancelEdit(): void {
    this.isEditingUsername = false;
  }

  deletePost(id: number): void {
    if (!confirm('Delete this post?')) return;
    this.http
      .delete<void>(`http://localhost:8080/api/posts/${id}`, { headers: this.getAuthHeaders() })
      .subscribe(() => this.loadUserPosts());
  }

  editPost(id: number): void {
    this.router.navigate(['/edit-post', id]);
  }

  private handleAuthError(err: any) {
    if (err.status === 401 || err.status === 403) {
      localStorage.removeItem('auth_token');
      this.router.navigate(['/auth']);
    } else {
      console.error(err);
    }
  }
}

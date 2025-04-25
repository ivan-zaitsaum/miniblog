import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent {
  isLoginMode = true;
  form = {
    username: '',
    email: '',
    password: ''
  };
  errorMessage: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  toggleMode(): void {
    this.isLoginMode = !this.isLoginMode;
    this.errorMessage = null;
  }

  submit(): void {
    if (this.isLoginMode) {
      this.authService.login({
        username: this.form.username,
        password: this.form.password
      }).subscribe({
        next: () => {
          alert('Login successful!');
          this.router.navigate(['/']);
        },
        error: err => {
          this.errorMessage = err.error?.message || 'Login failed.';
        }
      });
    } else {
      this.authService.register(this.form).subscribe({
        next: () => {
          alert('Registration successful! Please log in.');
          this.isLoginMode = true;
          this.form.password = '';
        },
        error: err => {
          this.errorMessage = err.error?.message || 'Registration failed.';
        }
      });
    }
  }
}

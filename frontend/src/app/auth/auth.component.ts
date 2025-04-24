import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
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

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  toggleMode(): void {
    this.isLoginMode = !this.isLoginMode;
  }

  submit(): void {
    if (this.isLoginMode) {
      this.authService.login(this.form).subscribe({
        next: () => {
          alert('Login successful!');
          location.href = '/';
        },
        error: err => {
          alert(err.error?.message || 'Login failed. Please try again.');
        }
      });
    } else {
      this.authService.register(this.form).subscribe({
        next: () => {
          alert('Registration successful! Please log in.');
          this.isLoginMode = true;
          this.form.password = ''; // clear password field
        },
        error: err => {
          alert(err.error?.message || 'Registration failed. Please try again.');
        }
      });
    }
  }
}

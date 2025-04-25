// src/app/posts/posts.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';  // Импортируем FormsModule
import { Post, PostService } from '../services/post.service';
import { AuthService } from '../services/auth.service';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-posts',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],  // Добавляем CommonModule и FormsModule
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.css']
})
export class PostsComponent implements OnInit {
  posts: Post[] = [];
  newPost: Post = { title: '', content: '' };

  constructor(private postService: PostService, public authService: AuthService) {}

  ngOnInit(): void {
    this.loadPosts();
    const token = this.authService.getToken();
    console.log('Токен: ', token);
    if (token) {
      console.log('Пользователь авторизован!');
    } else {
      console.log('Пользователь не авторизован');
    }
  }

  loadPosts(): void {
    this.postService.getAll().subscribe((posts: Post[]) => {
      console.log('Полученные посты:', posts);
      this.posts = posts;
    });
  }

  addPost(): void {
    if (!this.authService.isLoggedIn()) {
      console.log('Ошибка: пользователь не авторизован');
      return;
    }

    this.postService.create(this.newPost).subscribe((post: Post) => {
      this.posts.push(post);
      this.newPost = { title: '', content: '' };
    });
  }

  deletePost(id: number): void {
    if (confirm('Are you sure you want to delete this post?')) {
      this.postService.deletePost(id).subscribe(() => {
        this.loadPosts(); // Обновим список после удаления
      });
    }
  }
}

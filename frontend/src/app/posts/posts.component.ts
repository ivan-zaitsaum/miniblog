// src/app/posts/posts.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Post, PostService } from '../services/post.service';
import { AuthService } from '../services/auth.service';
import { RouterLink } from '@angular/router';
import { CommentsComponent } from '../comments/comments.component';
import { CategoryService } from '../services/category.service';
import { TagService } from '../services/tag.service';

@Component({
  selector: 'app-posts',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, CommentsComponent],
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.css']
})
export class PostsComponent implements OnInit {
  posts: Post[] = [];
  newPost: Post = { title: '', content: '' };

  searchQuery: string = '';
  selectedCategoryId: number | null = null;
  selectedTagId: number | null = null;

  categories: any[] = [];
  tags: any[] = [];

  constructor(
    private postService: PostService,
    public authService: AuthService,
    private categoryService: CategoryService,
    private tagService: TagService
  ) {}

  ngOnInit(): void {
    this.loadPosts();
    this.loadCategories();
    this.loadTags();

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

  loadCategories(): void {
    this.categoryService.getAll().subscribe(cats => this.categories = cats);
  }

  loadTags(): void {
    this.tagService.getAll().subscribe(tags => this.tags = tags);
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
        this.loadPosts();
      });
    }
  }

  applyFilter(): void {
    this.postService.getFiltered(this.searchQuery, this.selectedCategoryId, this.selectedTagId).subscribe(posts => {
      this.posts = posts;
    });
  }

  resetFilter(): void {
    this.searchQuery = '';
    this.selectedCategoryId = null;
    this.selectedTagId = null;
    this.loadPosts();
  }
}

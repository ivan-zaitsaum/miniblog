import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Post, PostService } from '../services/post.service';

@Component({
  selector: 'app-posts',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.css']
})
export class PostsComponent implements OnInit {
  posts: Post[] = [];
  newPost: Post = {
    title: '',
    content: '',
    authorUsername: ''
  };

  constructor(private postService: PostService) {}

  ngOnInit(): void {
    this.loadPosts();
  }

  loadPosts(): void {
    this.postService.getAll().subscribe(posts => this.posts = posts);
  }

  addPost(): void {
    if (this.newPost.title && this.newPost.content && this.newPost.authorUsername) {
      this.postService.create(this.newPost).subscribe(post => {
        this.posts.push(post);
        this.newPost = { title: '', content: '', authorUsername: '' };
      });
    }
  }
}

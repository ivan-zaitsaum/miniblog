import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Post, PostService } from '../services/post.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  imports: [
    FormsModule
  ],
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent {
  newPost: Post = { title: '', content: '' };

  constructor(private postService: PostService, private router: Router) {}

  submitPost(): void {
    this.postService.create(this.newPost).subscribe(() => {
      this.router.navigate(['/']); // вернуться на главную
    });
  }
}

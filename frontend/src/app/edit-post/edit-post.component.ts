import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Post, PostService } from '../services/post.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-edit-post',
  templateUrl: './edit-post.component.html',
  imports: [
    FormsModule
  ],
  styleUrls: ['./edit-post.component.css']
})
export class EditPostComponent implements OnInit {
  postId!: number;
  postData: Post = { title: '', content: '' };

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.postId = Number(this.route.snapshot.paramMap.get('id'));
    this.postService.getAll().subscribe(posts => {
      const post = posts.find(p => p.id === this.postId);
      if (post) {
        this.postData = { title: post.title, content: post.content };
      }
    });
  }

  updatePost(): void {
    this.postService.updatePost(this.postId, this.postData).subscribe(() => {
      this.router.navigate(['/']);
    });
  }
}

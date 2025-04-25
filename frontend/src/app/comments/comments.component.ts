import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommentService } from '../services/comment.service';
import { AuthService } from '../services/auth.service';
import { Comment } from '../models/comment.model';

@Component({
  selector: 'app-comments',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {
  @Input() postId!: number;
  comments: Comment[] = [];
  newComment: string = '';

  constructor(
    private commentService: CommentService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadComments();
  }

  loadComments(): void {
    this.commentService.getComments(this.postId).subscribe(data => {
      this.comments = data;
    });
  }

  submitComment(): void {
    if (!this.newComment.trim()) return;

    this.commentService.addComment(this.postId, this.newComment).subscribe(() => {
      this.newComment = '';
      this.loadComments();
    });
  }
}

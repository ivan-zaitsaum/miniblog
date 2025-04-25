import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Post, PostService } from '../services/post.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common'; // 👈 обязательно для *ngFor и *ngIf
import { CategoryService } from '../services/category.service';
import { TagService } from '../services/tag.service';
import { Category } from '../models/category.model';
import { Tag } from '../models/tag.model';

@Component({
  selector: 'app-add-post',
  standalone: true,
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css'],
  imports: [
    CommonModule, // 👈 добавили сюда
    FormsModule
  ]
})
export class AddPostComponent implements OnInit {
  newPost: Post = { title: '', content: '' };
  selectedCategories: number[] = [];
  selectedTags: number[] = [];

  categories: Category[] = [];
  tags: Tag[] = [];

  newCategoryName: string = '';
  newTagName: string = '';

  constructor(
    private postService: PostService,
    private categoryService: CategoryService,
    private tagService: TagService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.categoryService.getAll().subscribe(data => this.categories = data);
    this.tagService.getAll().subscribe(data => this.tags = data);
  }

  submitPost(): void {
    const payload = {
      ...this.newPost,
      categoryIds: this.selectedCategories,
      tagIds: this.selectedTags
    };

    this.postService.create(payload).subscribe(() => {
      this.router.navigate(['/']);
    });
  }

  onCheckboxChange(event: any, selectedList: number[]): void {
    const id = +event.target.value;
    if (event.target.checked) {
      selectedList.push(id);
    } else {
      const index = selectedList.indexOf(id);
      if (index !== -1) {
        selectedList.splice(index, 1);
      }
    }
  }

  createCategory(): void {
    if (!this.newCategoryName.trim()) return;

    this.categoryService.create(this.newCategoryName).subscribe({
      next: () => {
        this.newCategoryName = '';
        this.categoryService.getAll().subscribe(data => this.categories = data);
      },
      error: err => console.error('❌ Ошибка при создании категории:', err)
    });
  }

  createTag(): void {
    if (!this.newTagName.trim()) return;

    this.tagService.create(this.newTagName).subscribe({
      next: () => {
        this.newTagName = '';
        this.tagService.getAll().subscribe(data => this.tags = data);
      },
      error: err => console.error('❌ Ошибка при создании тега:', err)
    });
  }
}

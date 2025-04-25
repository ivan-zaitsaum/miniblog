package com.example.miniblog.controller;

import com.example.miniblog.dto.CreateCategoryRequest;
import com.example.miniblog.model.Category;
import com.example.miniblog.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        return ResponseEntity.ok(categoryRepository.save(category));
    }
}

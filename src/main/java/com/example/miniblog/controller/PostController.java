package com.example.miniblog.controller;

import com.example.miniblog.model.*;
import com.example.miniblog.repository.*;
import com.example.miniblog.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CategoryRepository categoryRepo;
    private final TagRepository tagRepo;
    private final CommentRepository commentRepo;

    public PostController(PostService postService,
                          CategoryRepository categoryRepo,
                          TagRepository tagRepo,
                          CommentRepository commentRepo) {
        this.postService = postService;
        this.categoryRepo = categoryRepo;
        this.tagRepo = tagRepo;
        this.commentRepo = commentRepo;
    }

    private static final int PAGE_SIZE = 5;

    @GetMapping
    public String list(
            @RequestParam Optional<String> q,
            @RequestParam Optional<Long> category,
            @RequestParam Optional<Long> tag,
            @RequestParam(defaultValue="1") int page,
            Model m
    ) {
        Page<Post> posts = postService.listAll(
                q.orElse(null),
                category.orElse(null),
                tag.orElse(null),
                PageRequest.of(page-1, PAGE_SIZE)
        );
        m.addAttribute("posts", posts);
        m.addAttribute("categories", categoryRepo.findAll());
        m.addAttribute("tags", tagRepo.findAll());
        m.addAttribute("searchQuery", q.orElse(""));
        m.addAttribute("selectedCategory", category.orElse(null));
        m.addAttribute("selectedTag", tag.orElse(null));
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", posts.getTotalPages());
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model m) {
        Post post = postService.getById(id).orElseThrow();
        m.addAttribute("post", post);
        return "posts/show";
    }

    @PostMapping("/{postId}/comments")
    public String addComment(
            @PathVariable Long postId,
            @RequestParam String content,
            @AuthenticationPrincipal Principal p
    ) {
        Post post = postService.getById(postId).orElseThrow();
        Comment c = new Comment();
        c.setContent(content);
        c.setPost(post);
        // сохраняем без авторизации, просто добавляем тело
        commentRepo.save(c);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/new")
    public String createForm(Model m) {
        m.addAttribute("post", new Post());
        m.addAttribute("categories", categoryRepo.findAll());
        m.addAttribute("tags", tagRepo.findAll());
        return "posts/form";
    }

    @PostMapping
    public String create(
            @ModelAttribute Post post,
            @RequestParam Set<Long> categoryIds,
            @RequestParam Set<Long> tagIds
    ) {
        post.setCategories(Set.copyOf(categoryRepo.findAllById(categoryIds)));
        post.setTags(Set.copyOf(tagRepo.findAllById(tagIds)));
        postService.save(post);
        return "redirect:/posts";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model m) {
        Post post = postService.getById(id).orElseThrow();
        m.addAttribute("post", post);
        m.addAttribute("categories", categoryRepo.findAll());
        m.addAttribute("tags", tagRepo.findAll());
        return "posts/form";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute Post post,
            @RequestParam Set<Long> categoryIds,
            @RequestParam Set<Long> tagIds
    ) {
        Post existing = postService.getById(id).orElseThrow();
        existing.setTitle(post.getTitle());
        existing.setContent(post.getContent());
        existing.setCategories(Set.copyOf(categoryRepo.findAllById(categoryIds)));
        existing.setTags(Set.copyOf(tagRepo.findAllById(tagIds)));
        postService.save(existing);
        return "redirect:/posts";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        postService.deleteById(id);
        return "redirect:/posts";
    }
}

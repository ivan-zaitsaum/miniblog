package com.example.miniblog.service;

import com.example.miniblog.dto.CreatePostRequest;
import com.example.miniblog.dto.PostDto;
import com.example.miniblog.model.Category;
import com.example.miniblog.model.Post;
import com.example.miniblog.model.Tag;
import com.example.miniblog.model.User;
import com.example.miniblog.repository.CategoryRepository;
import com.example.miniblog.repository.PostRepository;
import com.example.miniblog.repository.TagRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public PostServiceImpl(PostRepository postRepository,
                           CategoryRepository categoryRepository,
                           TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<PostDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(p -> new PostDto(
                        p.getId(),
                        p.getTitle(),
                        p.getContent(),
                        p.getAuthorUsername(),
                        p.getCreatedAt(),
                        p.getCategories().stream().map(Category::getName).collect(Collectors.toSet()),
                        p.getTags().stream().map(Tag::getName).collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Post createPost(CreatePostRequest request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setAuthor(user);
        post.setCreatedAt(java.time.LocalDateTime.now());

        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            post.setCategories(new HashSet<>(categoryRepository.findAllById(request.getCategoryIds())));
        }

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            post.setTags(new HashSet<>(tagRepository.findAllById(request.getTagIds())));
        }

        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own posts");
        }

        postRepository.delete(post);
    }

    @Override
    public boolean updatePost(Long id, CreatePostRequest request, String username) {
        return postRepository.findById(id).map(post -> {
            if (!post.getAuthor().getUsername().equals(username)) {
                return false;
            }

            post.setTitle(request.getTitle());
            post.setContent(request.getContent());
            postRepository.save(post);
            return true;
        }).orElse(false);
    }
}

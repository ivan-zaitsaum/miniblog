package com.example.miniblog.service;

import com.example.miniblog.dto.CreatePostRequest;
import com.example.miniblog.dto.PostDto;
import com.example.miniblog.model.Post;
import com.example.miniblog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(p -> new PostDto(
                        p.getId(),
                        p.getTitle(),
                        p.getContent(),
                        p.getAuthorUsername(),
                        p.getCreatedAt()
                )).collect(Collectors.toList());
    }
    @Override
    public Post createPost(CreatePostRequest request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthorUsername(request.getAuthorUsername());
        post.setCreatedAt(java.time.LocalDateTime.now());
        return postRepository.save(post);
    }
}

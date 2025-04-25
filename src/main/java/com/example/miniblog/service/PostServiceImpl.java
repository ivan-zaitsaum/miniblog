package com.example.miniblog.service;

import com.example.miniblog.dto.CreatePostRequest;
import com.example.miniblog.dto.PostDto;
import com.example.miniblog.model.Post;
import com.example.miniblog.model.User;
import com.example.miniblog.repository.PostRepository;
import org.springframework.security.core.context.SecurityContextHolder;
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

        // Извлекаем пользователя из SecurityContextHolder
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Присваиваем пользователя посту
        post.setAuthor(user);

        post.setCreatedAt(java.time.LocalDateTime.now());
        return postRepository.save(post);
    }

    // Логика для удаления поста
    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Получаем текущего авторизованного пользователя
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Проверяем, что текущий пользователь является автором поста
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own posts");
        }

        // Удаляем пост, если авторизованный пользователь — автор
        postRepository.delete(post);
    }

    @Override
    public boolean updatePost(Long id, CreatePostRequest request, String username) {
        return postRepository.findById(id).map(post -> {
            System.out.println("POST AUTHOR USERNAME: " + post.getAuthor().getUsername());
            System.out.println("TOKEN USERNAME: " + username);
            if (!post.getAuthor().getUsername().equals(username)) {
                return false; // не автор — нельзя редактировать
            }

            post.setTitle(request.getTitle());
            post.setContent(request.getContent());
            postRepository.save(post);
            return true;

        }).orElse(false); // если пост не найден

    }
}

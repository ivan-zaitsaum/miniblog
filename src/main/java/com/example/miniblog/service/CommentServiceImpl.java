package com.example.miniblog.service;

import com.example.miniblog.dto.CommentDto;
import com.example.miniblog.dto.CreateCommentRequest;
import com.example.miniblog.model.Comment;
import com.example.miniblog.model.Post;
import com.example.miniblog.model.User;
import com.example.miniblog.repository.CommentRepository;
import com.example.miniblog.repository.PostRepository;
import com.example.miniblog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return commentRepository.findByPostOrderByCreatedAtAsc(post).stream()
                .map(c -> new CommentDto(
                        c.getId(),
                        c.getContent(),
                        c.getAuthor().getUsername(),
                        c.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void addCommentToPost(Long postId, CreateCommentRequest request, String username) {
        Post post = postRepository.findById(postId).orElseThrow();
        User author = userRepository.findByUsername(username).orElseThrow();

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setContent(request.getContent());
        comment.setCreatedAt(java.time.LocalDateTime.now());

        commentRepository.save(comment);
    }
}

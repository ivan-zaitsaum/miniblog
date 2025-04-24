package com.example.miniblog.service;

import com.example.miniblog.model.Post;
import com.example.miniblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository repo;

    @Override
    public Page<Post> listAll(String q, Long categoryId, Long tagId, Pageable pageable) {
        if (categoryId != null)      return repo.findAllByCategories_Id(categoryId, pageable);
        else if (tagId != null)      return repo.findAllByTags_Id(tagId, pageable);
        else if (q != null && !q.isBlank()) return repo.findAllByTitleContainingIgnoreCase(q, pageable);
        else                         return repo.findAll(pageable);
    }

    @Override
    public Optional<Post> getById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Post save(Post post) {
        return repo.save(post);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}

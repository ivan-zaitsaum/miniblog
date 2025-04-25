package com.example.miniblog.controller;

import com.example.miniblog.dto.CreateTagRequest;
import com.example.miniblog.model.Tag;
import com.example.miniblog.repository.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@CrossOrigin(origins = "*")
public class TagController {
    private final TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody CreateTagRequest request) {
        Tag tag = new Tag();
        tag.setName(request.getName());
        return ResponseEntity.ok(tagRepository.save(tag));
    }
}

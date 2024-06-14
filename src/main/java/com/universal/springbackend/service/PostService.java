package com.universal.springbackend.service;

import com.universal.springbackend.entity.Post;
import com.universal.springbackend.repository.PostRepository;
import com.universal.springbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    public boolean postIdExists(Long id) {
        return postRepository.findById(id).isPresent();
    }

    public Post getPostById(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    public List<Post> searchByTitle(String title) {
        return postRepository.findByTitleContaining(title);
    }
}
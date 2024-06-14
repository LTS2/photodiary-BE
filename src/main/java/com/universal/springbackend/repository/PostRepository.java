package com.universal.springbackend.repository;

import com.universal.springbackend.entity.Post;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository {
    Post save(Post post);
    List<Post> findAll();
    Optional<Post> findById(Long id);
    List<Post> findByTitleContaining(String title);
}
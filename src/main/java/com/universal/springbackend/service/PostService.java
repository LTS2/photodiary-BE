package com.universal.springbackend.service;

import com.universal.springbackend.entity.Post;
import com.universal.springbackend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}

	public Post save(Post post) {
		return postRepository.save(post);
	}

	public Post getPostById(Long id) {
		return postRepository.findById(id).orElse(null);
	}

    public List<Post> searchByCaption(String keywords) {
        return postRepository.findByKeywordsContaining(keywords);

    }

	public void delete(Long id) {
		postRepository.deleteById(id);
	}
}

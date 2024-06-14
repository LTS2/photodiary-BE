package com.universal.springbackend.controller;

import com.universal.springbackend.entity.Post;
import com.universal.springbackend.entity.User;
import com.universal.springbackend.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@SessionAttributes("loginUser")
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // 전체 게시물 조회
    @GetMapping("/")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> postList = postService.getAllPosts();
        if (!postList.isEmpty()) {
            return new ResponseEntity<>(postList, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 게시물 작성
    @PostMapping("/")
    public ResponseEntity<Post> createPost(@RequestBody Post post, HttpSession session) {
        log.info(">>>>> PostController.createPost.executed()");
        User loginUser = (User) session.getAttribute("loginUser");
        String postAuthorId = String.valueOf(loginUser.getId());
        Post savedPost = postService.save(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    // 게시물 수정
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post updatedPost, HttpSession session) {
        log.info(">>>>> PostController.updatePost.executed()");
        User loginUserId = (User) session.getAttribute("loginUser");
        if (loginUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Post existingPost = postService.getPostById(id);
        if (existingPost == null) {
            return ResponseEntity.notFound().build();
        }
        if (!existingPost.getPostAuthorId().equals(loginUserId.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContents(updatedPost.getContents());
        existingPost.setFileName(updatedPost.getFileName());
        existingPost.setFilePath(updatedPost.getFilePath());
        Post savedPost = postService.save(existingPost);

        return ResponseEntity.ok(savedPost);
    }

    // 게시물 검색
    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPostsByTitle(@RequestParam String title) {
        log.info(">>>>> PostController.searchPostsByTitle.executed()");
        List<Post> foundPosts = postService.searchByTitle(title);
        if (!foundPosts.isEmpty()) {
            return ResponseEntity.ok(foundPosts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
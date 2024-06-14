package com.universal.springbackend.controller;

import com.universal.springbackend.dto.PostDTO;
import com.universal.springbackend.entity.Post;
import com.universal.springbackend.entity.User;
import com.universal.springbackend.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@SessionAttributes("loginUser")
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        log.info("getAllPosts().executed");
        List<Post> postList = postService.getAllPosts();
        List<PostDTO> postDTOList = postList.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(postDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(convertToDTO(post));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<PostDTO> createPost(@RequestParam(value = "image", required = false) MultipartFile image,
                                              @RequestParam("title") String title,
                                              @RequestParam("caption") String caption,
                                              @RequestParam("keywords") String keywords,
                                              HttpSession session) {
        log.info(">>>>> PostController.createPost.executed()");
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Post post = new Post();
            post.setTitle(title);
            post.setCaption(caption);
            post.setKeywords(keywords);
            post.setAuthor(loginUser);
            post.setCreatedDate(new Date());

            if (image != null && !image.isEmpty()) {
                log.info("Uploading image: " + image.getOriginalFilename());
                byte[] imageBytes = image.getBytes();
                log.info("Image size: " + imageBytes.length);
                post.setImage(imageBytes);
            } else {
                log.info("No image uploaded");
                post.setImage(null);
            }

            Post savedPost = postService.save(post);
            PostDTO postDTO = convertToDTO(savedPost);
            return new ResponseEntity<>(postDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long id, @RequestParam(value = "image", required = false) MultipartFile image,
                                              @RequestParam("title") String title,
                                              @RequestParam("caption") String caption,
                                              @RequestParam("keywords") String keywords,
                                              HttpSession session) {
        log.info(">>>>> PostController.updatePost.executed()");
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Post existingPost = postService.getPostById(id);
        if (existingPost == null) {
            return ResponseEntity.notFound().build();
        }
        if (!existingPost.getAuthor().getId().equals(loginUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (title.isEmpty() || caption.isEmpty() || keywords.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        existingPost.setTitle(title);
        existingPost.setCaption(caption);
        existingPost.setKeywords(keywords);

        if (image != null && !image.isEmpty()) {
            try {
                byte[] imageBytes = image.getBytes();
                existingPost.setImage(imageBytes);
            } catch (Exception e) {
                log.error("Error updating image", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        Post savedPost = postService.save(existingPost);
        PostDTO postDTO = convertToDTO(savedPost);

        return ResponseEntity.ok(postDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, HttpSession session) {
        log.info(">>>>> PostController.deletePost.executed()");
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Post existingPost = postService.getPostById(id);
        if (existingPost == null) {
            return ResponseEntity.notFound().build();
        }
        if (!existingPost.getAuthor().getId().equals(loginUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            postService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting post", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> searchPostsByTitle(@RequestParam String caption) {
        log.info(">>>>> PostController.searchPostsByTitle.executed()");
        List<Post> foundPosts = postService.searchByCaption(caption);
        List<PostDTO> postDTOList = foundPosts.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(postDTOList);
    }

    private PostDTO convertToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setCaption(post.getCaption());
        postDTO.setKeywords(post.getKeywords());
        postDTO.setCreatedDate(post.getCreatedDate());

        if (post.getAuthor() != null) {
            postDTO.setAuthorId(post.getAuthor().getId());
        } else {
            postDTO.setAuthorId(null);
        }

        if (post.getImage() != null) {
            postDTO.setImage(java.util.Base64.getEncoder().encodeToString(post.getImage()));
        } else {
            postDTO.setImage("");
        }

        return postDTO;
    }
}

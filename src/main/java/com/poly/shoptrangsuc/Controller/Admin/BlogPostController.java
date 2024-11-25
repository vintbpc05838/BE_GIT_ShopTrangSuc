package com.poly.shoptrangsuc.Controller.Admin;

import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Model.BlogPost;
import com.poly.shoptrangsuc.Repository.AccountRepository;
import com.poly.shoptrangsuc.Service.BlogPostService;
import com.poly.shoptrangsuc.Service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/news")
public class BlogPostController {
    @Autowired
    private BlogPostService blogPostService;

    private final JWTService jwtService;
    private final AccountRepository accountRepository;

    public BlogPostController(JWTService jwtService, AccountRepository accountRepository) {
        this.jwtService = jwtService;
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public ResponseEntity<List<BlogPost>> getAllBlogPosts() {
        List<BlogPost> blogPosts = blogPostService.getAllBlogPosts();
        return ResponseEntity.ok(blogPosts);
    }

    @PostMapping("/add")
    public ResponseEntity<BlogPost> createNewsPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image,
//            @RequestParam("productId") Integer productId,
            HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;

        String authorEmail = null;
        Account account = null; // Change to Account object
        if (token != null && !jwtService.isTokenExpired(token)) {
            authorEmail = jwtService.extractUsername(token);
            account = accountRepository.findAccountByEmail(authorEmail); // Fetch the account directly
        }

        if (account == null) {
            return ResponseEntity.status(404).body(null); // Handle the case where account is not found
        }

        BlogPost newPost = new BlogPost();
        newPost.setTitle(title);
        newPost.setContent(content);
        newPost.setAuthor(authorEmail);
        newPost.setAccount(account); // Set the Account object

        try {
            BlogPost createdPost = blogPostService.createPost(newPost, image);
            return ResponseEntity.ok(createdPost);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    // Phương thức sửa bài viết theo ID
    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updatePost(
            @PathVariable("id") Integer id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "productId", required = false) Integer productId, // Make this optional
            HttpServletRequest request) {
        try {
            BlogPost existingPost = blogPostService.getBlogPostById(id);
            if (existingPost == null) {
                return ResponseEntity.status(404).body(null); // Post not found
            }

            // Update post details
            existingPost.setTitle(title);
            existingPost.setContent(content);

            // Update image if provided
            if (image != null && !image.isEmpty()) {
                String imageUrl = blogPostService.uploadImageToCloudinary(image);
                existingPost.setImageUrl(imageUrl);
            }

            // Only update product if productId is provided
//            if (productId != null) {
//                Product product = blogPostService.getProductById(productId);
//                existingPost.setProduct(product);
//            }

            // Save the updated post
            BlogPost updatedPost = blogPostService.updatePost(existingPost);
            return ResponseEntity.ok(updatedPost);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null); // Error updating post
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Integer id, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;

        if (token == null || jwtService.isTokenExpired(token)) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid or expired token");
        }

        try {
            BlogPost post = blogPostService.getBlogPostById(id);
            if (post == null) {
                return ResponseEntity.status(404).body("Post not found");
            }

            // Delete the post
            blogPostService.deletePost(id);
            return ResponseEntity.ok("Post deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting post: " + e.getMessage());
        }
    }
}


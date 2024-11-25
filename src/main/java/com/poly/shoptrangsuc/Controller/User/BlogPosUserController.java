package com.poly.shoptrangsuc.Controller.User;

import com.poly.shoptrangsuc.Model.BlogPost;
import com.poly.shoptrangsuc.Service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/user/news")
public class BlogPosUserController {
    @Autowired
    private BlogPostService blogPostService;
    @GetMapping("/list")
    public ResponseEntity<List<BlogPost>> getAllBlogPosts() {
        List<BlogPost> blogPosts = blogPostService.getAllBlogPosts();
        return ResponseEntity.ok(blogPosts);
    }
    // Get a single article by postId
    @GetMapping("/list/{postId}")
    public ResponseEntity<List<BlogPost>> getArticleById(@PathVariable Integer postId) {
        BlogPost blogPosts = blogPostService.getBlogPostById(postId);
        if (blogPosts == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Collections.singletonList(blogPosts));
    }
}

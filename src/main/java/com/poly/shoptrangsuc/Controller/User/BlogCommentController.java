package com.poly.shoptrangsuc.Controller;


import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Model.BlogComment;
import com.poly.shoptrangsuc.Model.BlogPost;
import com.poly.shoptrangsuc.Repository.AccountRepository;
import com.poly.shoptrangsuc.Service.BlogCommentService;
import com.poly.shoptrangsuc.Service.BlogPostService;
import com.poly.shoptrangsuc.Service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/user/comments")
public class BlogCommentController {
    @Autowired
    private BlogPostService blogPostService;
    @Autowired
    private BlogCommentService blogCommentService;
    private final JWTService jwtService;
    private final AccountRepository accountRepository;

    public BlogCommentController(JWTService jwtService, AccountRepository accountRepository) {
        this.jwtService = jwtService;
        this.accountRepository = accountRepository;
    }


    @GetMapping("/post/{postId}")
    public ResponseEntity<List<BlogComment>> getCommentsByPostId(@PathVariable Integer postId) {
        List<BlogComment> comments = blogCommentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<BlogComment> addComment(@RequestBody BlogComment comment,
                                                  @RequestParam Integer postId,
                                                  HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;

        Account account = null;
        if (token != null && !jwtService.isTokenExpired(token)) {
            String authorEmail = jwtService.extractUsername(token);
            account = accountRepository.findAccountByEmail(authorEmail);
        }

        if (account == null) {
            return ResponseEntity.status(404).body(null);
        }

        // Truy vấn BlogPost từ cơ sở dữ liệu
        BlogPost blogPost = blogPostService.getBlogPostById(postId); // Giả sử bạn có service để tìm BlogPost

        if (blogPost == null) {
            return ResponseEntity.status(404).body(null); // Xử lý trường hợp không tìm thấy BlogPost
        }

        // Tạo mới bình luận
        BlogComment blogComment = new BlogComment();
        blogComment.setContent(comment.getContent());
        blogComment.setCommentDate(new Date(System.currentTimeMillis()));
        blogComment.setCommentName(account.getFullname());
        blogComment.setBlogPost(blogPost); // Thiết lập BlogPost đã truy vấn
        blogComment.setAccount(account);

        // Lưu bình luận vào cơ sở dữ liệu
        BlogComment savedComment = blogCommentService.addComment(blogComment);
        return ResponseEntity.ok(savedComment);
    }
    // Phương thức xóa bình luận
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;

        Account account = null;
        if (token != null && !jwtService.isTokenExpired(token)) {
            String authorEmail = jwtService.extractUsername(token);
            account = accountRepository.findAccountByEmail(authorEmail);
        }

        if (account == null) {
            return ResponseEntity.status(404).build();
        }

        // Kiểm tra xem bình luận có tồn tại không
        BlogComment existingComment = blogCommentService.getCommentById(commentId);
        if (existingComment == null) {
            return ResponseEntity.status(404).build(); // Không tìm thấy bình luận
        }

        // Kiểm tra quyền xóa (nếu cần thiết)
        if (!existingComment.getAccount().equals(account)) {
            return ResponseEntity.status(403).build(); // Không có quyền xóa bình luận này
        }

        // Thực hiện xóa bình luận
        blogCommentService.deleteComment(commentId);
        return ResponseEntity.noContent().build(); // Trả về 204 No Content
    }
}
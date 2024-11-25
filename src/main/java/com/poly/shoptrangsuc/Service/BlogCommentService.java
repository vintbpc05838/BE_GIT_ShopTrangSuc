package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.Model.BlogComment;
import com.poly.shoptrangsuc.Repository.BlogCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogCommentService {
    @Autowired
    private BlogCommentRepository blogCommentRepository;

    public List<BlogComment> getCommentsByPostId(Integer postId) {
        return blogCommentRepository.findByBlogPost_PostId(postId);
    }
    public BlogComment addComment(BlogComment comment) {
        return blogCommentRepository.save(comment);
    }
    // Lấy bình luận theo ID
    public BlogComment getCommentById(Integer commentId) {
        return blogCommentRepository.findById(commentId).orElse(null);
    }

    // Xóa bình luận theo ID
    public void deleteComment(Integer commentId) {
        blogCommentRepository.deleteById(commentId);
    }
}

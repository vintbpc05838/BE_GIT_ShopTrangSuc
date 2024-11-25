package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogCommentRepository extends JpaRepository<BlogComment,Integer> {
    List<BlogComment> findByBlogPost_PostId(Integer postId);
}

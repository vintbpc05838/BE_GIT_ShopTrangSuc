package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
    BlogPost findByPostId(Integer postId);
}

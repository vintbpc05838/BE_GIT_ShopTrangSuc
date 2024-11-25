package com.poly.shoptrangsuc.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.poly.shoptrangsuc.Model.BlogPost;
import com.poly.shoptrangsuc.Model.Product;
import com.poly.shoptrangsuc.Repository.AccountRepository;
import com.poly.shoptrangsuc.Repository.BlogPostRepository;
import com.poly.shoptrangsuc.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Cloudinary cloudinary;

    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }
    public BlogPost createPost(BlogPost blogPost, MultipartFile imageFile) throws IOException {
        // Upload image to Cloudinary
        if (imageFile != null && !imageFile.isEmpty()) {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");
            blogPost.setImageUrl(imageUrl);
        }

        // Set the post date
        blogPost.setPostDate(new java.sql.Date(System.currentTimeMillis()));

        // Set Product and Account
//        if (productId != null) {
//            Product product = productRepository.findById(productId).orElse(null);
//            blogPost.setProduct(product);
//        }

        // Save the blog post
        return blogPostRepository.save(blogPost);
    }
    public BlogPost getBlogPostById(Integer postId) {
        return blogPostRepository.findById(postId).orElse(null);
    }
    public Product getProductById(Integer productId) {
        return productRepository.findById(productId).orElse(null);
    }
    public String uploadImageToCloudinary(MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("secure_url");
        }
        return null;
    }
    public BlogPost updatePost(BlogPost blogPost) {
        return blogPostRepository.save(blogPost); // Lưu bài viết đã sửa vào cơ sở dữ liệu
    }
    public void deletePost(Integer id) {
        blogPostRepository.deleteById(id);
    }

}
package com.poly.shoptrangsuc.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Blog_Post")
@AllArgsConstructor
@NoArgsConstructor
@Data @Getter
@Setter
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "post_date")
    private java.sql.Date postDate;

    @Column(name = "author")
    private String author;

    @Column(name = "image_url")
    private String imageUrl;

//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    // Constructor nhận postId
    public BlogPost(Integer postId) {
        this.postId = postId;
    }

}

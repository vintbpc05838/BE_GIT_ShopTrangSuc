package com.poly.shoptrangsuc.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostDTO {
    private String title;
    private String content;
    private Date postDate;
    private String username;
    private String productName;
    private String image;


}

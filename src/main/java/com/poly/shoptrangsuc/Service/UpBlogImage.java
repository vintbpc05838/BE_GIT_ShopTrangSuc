package com.poly.shoptrangsuc.Service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface UpBlogImage {
    String uploadImage(MultipartFile file, Integer postId) throws IOException;
}

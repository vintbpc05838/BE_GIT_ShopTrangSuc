package com.poly.shoptrangsuc.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadImageFile {
    String uploadImage(MultipartFile file, Integer productId) throws IOException;
}

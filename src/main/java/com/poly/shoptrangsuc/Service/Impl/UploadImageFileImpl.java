package com.poly.shoptrangsuc.Service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.poly.shoptrangsuc.Model.Images;
import com.poly.shoptrangsuc.Repository.ImagesRepository;
import com.poly.shoptrangsuc.Repository.ProductRepository;
import com.poly.shoptrangsuc.Service.UploadImageFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadImageFileImpl implements UploadImageFile {

//    private final Cloudinary cloudinary;
//    private final ImagesRepository imagesRepository;
//    private final ProductRepository productRepository; // Thêm ProductRepository
//
//    @Override
//    public String uploadImage(MultipartFile file, Integer productId) throws IOException {
//        // Upload hình ảnh lên Cloudinary
//        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//        String imageUrl = (String) uploadResult.get("url"); // Lấy URL của hình ảnh
//
//        // Tạo đối tượng Images và liên kết với Product
//        Images image = new Images();
//        image.setImageUrl(imageUrl);
//        image.setDescribe("Uploaded image");
//
//        // Tìm Product theo ID
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        // Gán Product cho hình ảnh
//        image.setProduct(product);
//        imagesRepository.save(image); // Lưu ảnh vào DB
//
//        return imageUrl; // Trả về URL của ảnh đã upload
//    }

    private final Cloudinary cloudinary;
    private final ImagesRepository imagesRepository;
    private final ProductRepository productRepository;

    @Override
    public String uploadImage(MultipartFile file, Integer productId) throws IOException {
        // Upload hình ảnh lên Cloudinary
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = (String) uploadResult.get("url"); // Lấy URL của hình ảnh

        // Tạo đối tượng Images và liên kết với Product
        Images image = new Images();
        image.setImageUrl(imageUrl);
        image.setDescribe("Uploaded image");

        // Tìm Product theo ID nếu có
//        if (productId != null) {
//            Product product = productRepository.findById(productId)
//                    .orElseThrow(() -> new RuntimeException("Product not found"));
//            image.setProduct(product);
//        }

        imagesRepository.save(image); // Lưu ảnh vào DB

        return imageUrl; // Trả về URL của ảnh đã upload
    }


}



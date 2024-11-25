package com.poly.shoptrangsuc.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.poly.shoptrangsuc.Model.*;
import com.poly.shoptrangsuc.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ProductDetailService {

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private CloudinaryService cloudinaryService;  // You have CloudinaryService, so use it!
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ProductCategoryRepository categoryRepository;

    public List<ProductDetails> getAllProductDetails() {
        return productDetailsRepository.findAll();
    }

    public ProductDetails addProductDetails(ProductDetails productDetails, List<MultipartFile> imageFiles) throws IOException {
        // Save product details first
        ProductDetails savedProduct = productDetailsRepository.save(productDetails);

        // Check if images are provided, if not, skip the image upload process
        if (imageFiles != null && !imageFiles.isEmpty()) {
            // Upload images to Cloudinary and save the image details in the database
            for (MultipartFile imageFile : imageFiles) {
                // Debugging logs: Check if the image is being received
                System.out.println("Received image: " + imageFile.getOriginalFilename());

                // Upload image to Cloudinary using CloudinaryService
                Map<String, String> uploadResult = cloudinaryService.uploadImage(imageFile);
                String imageUrl = uploadResult.get("secure_url");

                // Debugging logs: Check if Cloudinary URL is returned correctly
                System.out.println("Image uploaded to Cloudinary: " + imageUrl);

                // Create image entity
                Images image = new Images();
                image.setImageUrl(imageUrl);
                image.setDescribe("Product image");
                image.setProductDetails(savedProduct); // Set the relationship with the product

                // Save image in the database
                imagesRepository.save(image);
            }
        }

        return savedProduct;
    }
    public Product addProduct(Product product) throws IOException {
        // Save the product
        return productRepository.save(product);
    }
    public ProductDetails getProductDetailsById(Integer id) {
        return productDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductDetails not found with id: " + id));
    }
    public ProductDetails updateProductDetails(ProductDetails productDetails, List<MultipartFile> imageFiles) throws IOException {
        // Save updated product details
        ProductDetails savedProduct = productDetailsRepository.save(productDetails);

        // Handle image updates if any new images are provided
        if (imageFiles != null && !imageFiles.isEmpty()) {
            // First, delete existing images if needed
            imagesRepository.deleteByProductDetails(savedProduct);

            // Upload new images to Cloudinary and save in database
            for (MultipartFile imageFile : imageFiles) {
                Map<String, String> uploadResult = cloudinaryService.uploadImage(imageFile);
                String imageUrl = uploadResult.get("secure_url");

                Images image = new Images();
                image.setImageUrl(imageUrl);
                image.setDescribe("Updated product image");
                image.setProductDetails(savedProduct); // Set the relationship with the product

                imagesRepository.save(image);
            }
        }

        return savedProduct;
    }
    // Lấy danh sách sản phẩm chưa bị xóa
    public List<ProductDetails> getActiveProducts() {
        return productDetailsRepository.findAllActiveProducts();
    }

    // Lấy danh sách sản phẩm trong thùng rác
    public List<ProductDetails> getDeletedProducts() {
        return productDetailsRepository.findAllDeletedProducts();
    }

    // Xóa sản phẩm (chuyển vào thùng rác)
    public void moveToTrash(Integer detailProductId) {
        ProductDetails product = productDetailsRepository.findById(detailProductId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + detailProductId));
        product.setDeleted(true); // Đánh dấu sản phẩm đã bị xóa
        productDetailsRepository.save(product);
    }
    // Khôi phục sản phẩm từ thùng rác
    public void restoreFromTrash(Integer detailProductId) {
        ProductDetails product = productDetailsRepository.findById(detailProductId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + detailProductId));
        product.setDeleted(false); // Đánh dấu sản phẩm chưa bị xóa
        productDetailsRepository.save(product);
    }
    @Transactional
    public void deleteProductAndDetails(Integer productId) {
        // Tìm sản phẩm theo ID
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Xóa ProductDetails liên quan
        ProductDetails productDetails = product.getProductDetails();
        if (productDetails != null) {
            productDetailsRepository.delete(productDetails);
        }

        // Xóa Product
        productRepository.delete(product);
    }
    // Product ingredients
    public List<ProductCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }
}


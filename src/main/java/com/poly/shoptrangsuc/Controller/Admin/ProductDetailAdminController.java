package com.poly.shoptrangsuc.Controller.Admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.shoptrangsuc.Model.*;
import com.poly.shoptrangsuc.Repository.MaterialRepository;
import com.poly.shoptrangsuc.Repository.ProductCategoryRepository;
import com.poly.shoptrangsuc.Repository.ProductRepository;
import com.poly.shoptrangsuc.Repository.SizeRepository;
import com.poly.shoptrangsuc.Service.ProductDetailService;
import com.poly.shoptrangsuc.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/product-details")
public class ProductDetailAdminController {
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductRepository productRepository;
    private final ProductDetailService productDetailService;

    public ProductDetailAdminController(ProductDetailService productDetailService) {
        this.productDetailService = productDetailService;
    }

    // GET endpoint
    @GetMapping()
    public ResponseEntity<List<ProductDetails>> getAllProducts_Admin() {
        List<ProductDetails> products = productDetailService.getAllProductDetails();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(
            @RequestParam String productName,
            @RequestParam BigDecimal price,
            @RequestParam Integer quantity,
            @RequestParam String status,
            @RequestParam String describe,
            @RequestParam Integer materialId,
            @RequestParam Integer sizeId,
            @RequestParam Integer categoryId,
            @RequestParam(required = false) List<MultipartFile> images) throws IOException {

        // Fetch the Material, Size, and ProductCategory from the database
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new IllegalArgumentException("Material not found with id: " + materialId));
        Size size = sizeRepository.findById(sizeId)
                .orElseThrow(() -> new IllegalArgumentException("Size not found with id: " + sizeId));
        ProductCategory productCategory = productCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        // Create and set product details
        ProductDetails productDetails = new ProductDetails();
        productDetails.setProductName(productName);
        productDetails.setPrice(price);
        productDetails.setQuantity(quantity);
        productDetails.setStatus(status);
        productDetails.setDescribe(describe);
        productDetails.setMaterial(material);
        productDetails.setSize(size);
        productDetails.setProductCategory(productCategory);
        productDetails.setDateAdded(LocalDate.now());

        // Save ProductDetails to database
        ProductDetails savedProductDetails = productDetailService.addProductDetails(productDetails, images);

        // Create a new Product object
        Product product = new Product();
        product.setProductName(productName);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setStatus(status);
        product.setDateAdded(LocalDate.now());
        product.setProductDetails(savedProductDetails);

        // Save the new product
        Product savedProduct = productDetailService.addProduct(product);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
//    @PostMapping("/update/{id}")
//    public ResponseEntity<Product> updateProduct(
//            @PathVariable Integer id,
//            @RequestParam String productName,
//            @RequestParam BigDecimal price,
//            @RequestParam Integer quantity,
//            @RequestParam String status,
//            @RequestParam String describe,
//            @RequestParam Integer materialId,
//            @RequestParam Integer sizeId,
//            @RequestParam Integer categoryId,
//            @RequestParam(required = false) List<MultipartFile> images) throws IOException {
//
//        // Fetch existing product details
//        ProductDetails existingProductDetails = productDetailService.getProductDetailsById(id);
//        if (existingProductDetails == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        // Update product details
//        existingProductDetails.setProductName(productName);
//        existingProductDetails.setPrice(price);
//        existingProductDetails.setQuantity(quantity);
//        existingProductDetails.setStatus(status);
//        existingProductDetails.setDescribe(describe);
//        existingProductDetails.setMaterial(materialRepository.findById(materialId)
//                .orElseThrow(() -> new IllegalArgumentException("Material not found with id: " + materialId)));
//        existingProductDetails.setSize(sizeRepository.findById(sizeId)
//                .orElseThrow(() -> new IllegalArgumentException("Size not found with id: " + sizeId)));
//        existingProductDetails.setProductCategory(productCategoryRepository.findById(categoryId)
//                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId)));
//
//        // Update product details in the database
//        ProductDetails updatedProductDetails = productDetailService.updateProductDetails(existingProductDetails, images);
//
//        // Create a new Product object (or update the existing one)
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
//        product.setProductName(productName);
//        product.setPrice(price);
//        product.setQuantity(quantity);
//        product.setStatus(status);
//        product.setDateAdded(LocalDate.now()); // Optionally update the date
//
//        // Save the updated product
//        Product savedProduct = productDetailService.addProduct(product);
//
//        return new ResponseEntity<>(savedProduct, HttpStatus.OK);
//    }
@PostMapping("/update/{id}")
public ResponseEntity<Product> updateProduct(
        @PathVariable Integer id,
        @RequestParam String productName,
        @RequestParam BigDecimal price,
        @RequestParam Integer quantity,
        @RequestParam String status,
        @RequestParam String describe,
        @RequestParam Integer materialId,
        @RequestParam Integer sizeId,
        @RequestParam Integer categoryId,
        @RequestParam(required = false) List<MultipartFile> images) throws IOException {

    // Fetch existing product details
    ProductDetails existingProductDetails = productDetailService.getProductDetailsById(id);
    if (existingProductDetails == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update product details
    existingProductDetails.setProductName(productName);
    existingProductDetails.setPrice(price);
    existingProductDetails.setQuantity(quantity);
    existingProductDetails.setStatus(status);
    existingProductDetails.setDescribe(describe);
    existingProductDetails.setMaterial(materialRepository.findById(materialId)
            .orElseThrow(() -> new IllegalArgumentException("Material not found with id: " + materialId)));
    existingProductDetails.setSize(sizeRepository.findById(sizeId)
            .orElseThrow(() -> new IllegalArgumentException("Size not found with id: " + sizeId)));
    existingProductDetails.setProductCategory(productCategoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId)));

    // Update product details in the database
    ProductDetails updatedProductDetails = productDetailService.updateProductDetails(existingProductDetails, images);

    // Use detail_product_id to find the corresponding Product
    Product product = productRepository.findByProductDetails_DetailProductId(existingProductDetails.getDetailProductId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + existingProductDetails.getDetailProductId()));

    // Update product properties
    product.setProductName(productName);
    product.setPrice(price);
    product.setQuantity(quantity);
    product.setStatus(status);
    product.setDateAdded(LocalDate.now()); // Optionally update the date

    // Save the updated product
    Product savedProduct = productDetailService.addProduct(product);

    return new ResponseEntity<>(savedProduct, HttpStatus.OK);
}
    // Lấy danh sách sản phẩm chưa bị xóa
    @GetMapping("/list")
    public ResponseEntity<List<ProductDetails>> getActiveProducts() {
        return ResponseEntity.ok(productDetailService.getActiveProducts());
    }

    // Lấy danh sách sản phẩm trong thùng rác
    @GetMapping("/trash")
    public ResponseEntity<List<ProductDetails>> getDeletedProducts() {
        return ResponseEntity.ok(productDetailService.getDeletedProducts());
    }

    // Xóa sản phẩm (chuyển vào thùng rác)
    @PutMapping("/move-to-trash/{id}")
    public ResponseEntity<String> moveToTrash(@PathVariable Integer id) {
        productDetailService.moveToTrash(id);
        return ResponseEntity.ok("Đã chuyển sản phẩm vào thùng rác.");
    }
    // Khôi phục sản phẩm từ thùng rác
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreFromTrash(@PathVariable Integer id) {
        productDetailService.restoreFromTrash(id);
        return ResponseEntity.ok("Đã khôi phục sản phẩm từ thùng rác.");
    }
    // Xóa vĩnh viên sản phẩm và chi tiết
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        productDetailService.deleteProductAndDetails(id);
        return ResponseEntity.ok("Sản phẩm và chi tiết sản phẩm đã được xóa thành công.");
    }
    // Product ingredients
     @GetMapping("/materials")
     public List<Material> getAllMaterials() {
         return productDetailService.getAllMaterials();
     }
    @GetMapping("/sizes")
    public List<Size> getAllSizes() {
        return productDetailService.getAllSizes();
    }
    @GetMapping("/categories")
    public List<ProductCategory> getAllCategories() {
        return productDetailService.getAllCategories();
    }
}




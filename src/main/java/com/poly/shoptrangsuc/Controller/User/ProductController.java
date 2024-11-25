package com.poly.shoptrangsuc.Controller.User;

import com.poly.shoptrangsuc.Model.Product;
import com.poly.shoptrangsuc.Model.ProductDetails;
import com.poly.shoptrangsuc.Repository.ProductDetailsRepository;
import com.poly.shoptrangsuc.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
    @GetMapping("/{productId}/related")
    public ResponseEntity<?> getRelatedProducts(@PathVariable Integer productId) {
        try {
            ProductDetails productDetails = productDetailsRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));

            List<ProductDetails> relatedProducts = productDetailsRepository.findByProductCategoryAndDeletedFalse(
                    productDetails.getProductCategory());

            return ResponseEntity.ok(relatedProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching related products");
        }
    }
}

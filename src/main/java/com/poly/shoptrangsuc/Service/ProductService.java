package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.DTO.ProductDTO;
import com.poly.shoptrangsuc.Model.Product;
import com.poly.shoptrangsuc.Model.ProductDetails;
import com.poly.shoptrangsuc.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findByProductDetailsDeletedIsFalse();
    }
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }
}
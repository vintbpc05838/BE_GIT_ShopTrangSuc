package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.DTO.ProductDTO;
import com.poly.shoptrangsuc.Model.*;
import com.poly.shoptrangsuc.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
//import java.sql.Date;
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

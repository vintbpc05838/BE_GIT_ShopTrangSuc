package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    ProductCategory findByCategoryName(String categoryName);
}

package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<ProductCategory, Integer> {
    // Các phương thức truy vấn cho bảng Category (nếu cần)
}

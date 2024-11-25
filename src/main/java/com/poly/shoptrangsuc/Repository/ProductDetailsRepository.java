package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.ProductCategory;
import com.poly.shoptrangsuc.Model.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Integer> {
    // Lấy danh sách sản phẩm chưa bị xóa
    @Query("SELECT p FROM ProductDetails p WHERE p.deleted = false")
    List<ProductDetails> findAllActiveProducts();

    // Lấy danh sách sản phẩm đã bị xóa (trong thùng rác)
    @Query("SELECT p FROM ProductDetails p WHERE p.deleted = true")
    List<ProductDetails> findAllDeletedProducts();

    List<ProductDetails> findByProductCategoryAndDeletedFalse(ProductCategory productCategory);
}

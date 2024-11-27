package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Integer> {
    // Các phương thức truy vấn tùy chỉnh nếu cần
}

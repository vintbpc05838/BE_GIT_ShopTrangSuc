package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Integer> {
    // Các phương thức truy vấn cho bảng Size (nếu cần)
}

package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Integer> {
    Size findBySizeDescription(Integer sizeDescription);  // Tìm kiếm Size theo mô tả kích thước
}

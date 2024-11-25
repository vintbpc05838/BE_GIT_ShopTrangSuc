package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
}

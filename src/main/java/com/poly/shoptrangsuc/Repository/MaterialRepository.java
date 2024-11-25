package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
}


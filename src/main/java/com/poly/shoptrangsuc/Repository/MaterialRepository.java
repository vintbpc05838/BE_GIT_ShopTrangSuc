package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    Material findByMaterialName(String materialName);
}


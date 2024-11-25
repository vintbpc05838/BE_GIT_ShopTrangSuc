package com.poly.shoptrangsuc.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Product_Category")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "category_name", length = 100)
    private String categoryName;

//    @OneToMany(mappedBy = "category")
//    List<ProductDetails> productDetails;
}

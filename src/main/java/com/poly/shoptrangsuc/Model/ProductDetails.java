package com.poly.shoptrangsuc.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Product_Details")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_product_id")
    private Integer detailProductId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status")
    private String status;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "describe")
    private String describe;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private Boolean deleted = false; // Đánh dấu sản phẩm đã bị xóa

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory productCategory;

    // @JsonManagedReference: giúp tránh vòng lặp vô hạn và serialize ProductDetails với danh sách Images
    @JsonManagedReference
    @OneToMany(mappedBy = "productDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Images> images = new ArrayList<>();
}

package com.poly.shoptrangsuc.Model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Product")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "describe")
    private String describe;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "status")
    private String status;

//    @ManyToOne
//    @JoinColumn(name = "image_id") // Reference the primary image for the product
//    private Images images;

    @ManyToOne
    @JoinColumn(name = "detail_product_id") // Update the column name here
    private ProductDetails productDetails;
}
package com.poly.shoptrangsuc.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DiscountType")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiscountType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DiscountTypeID")
    private Integer discountTypeId;

    @Column(name = "DiscountName", nullable = false)
    private String discountName;

    @Column(name = "DiscountRate", nullable = false)
    private String discountRate;
}

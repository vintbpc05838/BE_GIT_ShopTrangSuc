package com.poly.shoptrangsuc.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Images")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer imageId;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "describe", length = 255)
    private String describe;

    // @JsonBackReference: tránh việc serialize lại ProductDetails trong đối tượng Images
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "detail_product_id")
    private ProductDetails productDetails; // This links to ProductDetails
}

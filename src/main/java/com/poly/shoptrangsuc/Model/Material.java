package com.poly.shoptrangsuc.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Material")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id")
    private Integer materialId;

    @Column(name = "material_name", nullable = false, length = 50)
    private String materialName;

    @Column(name = "describe", length = 255)
    private String describe;

//    @OneToMany(mappedBy = "material")
//    List<ProductDetails> productDetails;
}
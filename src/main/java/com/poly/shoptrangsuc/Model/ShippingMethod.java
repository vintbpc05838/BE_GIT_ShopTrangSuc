package com.poly.shoptrangsuc.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Shipping_Method")

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShippingMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_method_id")
    private Integer shippingMethodId;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "shipping_fee")
    private String shippingFee;

    // Constructor with shippingMethodId
    public ShippingMethod(Integer shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
        this.methodName = null;  // default value
        this.shippingFee = null;  // default value
    }

    // Nếu bạn không cần cascade nữa, thì bỏ đi @OneToMany hoặc các mối quan hệ tương tự ở đây.
    // @OneToMany(mappedBy = "shippingMethod")
    // private Order order;
}

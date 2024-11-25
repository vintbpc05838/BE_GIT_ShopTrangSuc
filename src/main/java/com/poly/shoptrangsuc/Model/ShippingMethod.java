package com.poly.shoptrangsuc.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ShippingMethod")
public class ShippingMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ShippingMethodID")
    private Integer shippingMethodId;

    @Column(name = "MethodName", length = 50)
    private String methodName;

    @Column(name = "ShippingFee", precision = 10, scale = 2)
    private BigDecimal shippingFee;
    @OneToOne
    @JoinColumn(name = "OrderID")
    private Order orders;
    // Getters and Setters

    public Order getOrders() {
        return orders;
    }

    public void setOrders(Order orders) {
        this.orders = orders;
    }

    public Integer getShippingMethodId() {
        return shippingMethodId;
    }

    public void setShippingMethodId(Integer shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }
}
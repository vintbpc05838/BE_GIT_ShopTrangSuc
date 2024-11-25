package com.poly.shoptrangsuc.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Integer orderId;
    private Date orderDate;
    private BigDecimal totatAmount;
    private Integer promotionalDiscount;
    private Integer vipDiscount;
    private String address;
    private BigDecimal totatAmountAfterDiscount;
    private String shippingMethodName;
    private String customerName;
    private String orderStatusName;
    private String paymentMethod;
}
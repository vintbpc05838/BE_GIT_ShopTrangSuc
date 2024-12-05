package com.poly.shoptrangsuc.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentDTO {
    private Integer paymentId;
    private BigDecimal amount;
    private String paymentStatus;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private Integer orderId; // Để quản lý quan hệ với thực thể Order
}

package com.poly.shoptrangsuc.VNPAY;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class VNPayRequest {
    Integer total;      // Tổng tiền thanh toán
    String orderInfo;   // Thông tin đơn hàng
    String returnUrl;   // URL trả về sau khi thanh toán
}

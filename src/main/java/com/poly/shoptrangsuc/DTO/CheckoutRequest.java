package com.poly.shoptrangsuc.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckoutRequest {
    private Integer orderId;
    private String address; // Địa chỉ giao hàng
    private boolean vipMember; // Thành viên VIP hay không
    private String paymentMethod; // Phương thức thanh toán (COD, VNPAY, ...)
    private Integer shippingMethodId; // Phương thức vận chuyển ID
    private Integer accountId; // ID tài khoản đặt hàng
    private List<ProductDTO> products; // Danh sách sản phẩm
    private String shippingMethod; // DELIVERY hoặc PICKUP
    private String customerEmail;

    private String districtId;  // ID quận/huyện của người nhận
    private String wardCode;  // Mã xã/phường của người nhận
    private BigDecimal totalAmount;  // Tổng số tiền cần thanh toán
    private String returnUrl;
}

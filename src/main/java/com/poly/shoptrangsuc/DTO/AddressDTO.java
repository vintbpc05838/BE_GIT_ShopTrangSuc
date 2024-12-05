package com.poly.shoptrangsuc.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressDTO {
    private Integer addressId;
    private String address;
    private String city;
    private String district;
    private String ward; // Thêm trường ward
    private Boolean isDefault;

    // Nếu cần thêm constructor riêng để hỗ trợ các trường hợp khác, bạn có thể tạo thêm ở đây.
}

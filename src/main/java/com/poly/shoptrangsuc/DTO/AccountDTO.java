package com.poly.shoptrangsuc.DTO;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDTO {
    private String fullname;        // Họ và tên
    private String email;           // Email
    private String phone;           // Số điện thoại
    private String gender;          // Giới tính
    private String password;        // Mật khẩu
    private String role;            // Vai trò (ADMIN, USER, STAFF)
    private String status;          // Trạng thái tài khoản
    private String token;           // Mã token
    private List<AddressDTO> addresses; // Danh sách địa chỉ
}

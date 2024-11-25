package com.poly.shoptrangsuc.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private String fullname;
    private String email;
    private String phone;
    private String gender; // Giới tính
    private String password; // Mật khẩu
    private String role; // Vai trò (ADMIN, USER, STAFF)
    private String status; // Trạng thái tài khoản
    private String token;

   private List<AddressDTO> addresses;


}

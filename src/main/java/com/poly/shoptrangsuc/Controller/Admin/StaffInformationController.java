package com.poly.shoptrangsuc.Controller.Admin;

import com.poly.shoptrangsuc.DTO.AccountDTO;
import com.poly.shoptrangsuc.DTO.AddressDTO;
import com.poly.shoptrangsuc.DTO.ChangePasswordDTO;
import com.poly.shoptrangsuc.Service.StaffInformatiomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/staff")
public class StaffInformationController {

    @Autowired
    private StaffInformatiomService staffInformatiomService;

    // Endpoint để lấy thông tin tài khoản của người dùng đang đăng nhập
    @GetMapping("/staff-information")
    public ResponseEntity<AccountDTO> getAccountDetails(@RequestHeader("Authorization") String token) {
        try {
            // Loại bỏ "Bearer " nếu token có prefix
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Gọi service để lấy thông tin tài khoản
            AccountDTO accountDTO = staffInformatiomService.getAccountDetails(token);

            // Trả về thông tin tài khoản dưới dạng ResponseEntity
            return ResponseEntity.ok(accountDTO);
        } catch (Exception e) {
            // Trả về lỗi nếu có exception (ví dụ: token không hợp lệ hoặc tài khoản không tồn tại)
            return ResponseEntity.status(400).body(null);
        }
    }



    @PutMapping("/save/staff-information")
    public ResponseEntity<?> saveAccountDetails(@RequestBody AccountDTO accountDTO) {
        if (accountDTO.getAddresses() == null || accountDTO.getAddresses().isEmpty()) {
            return ResponseEntity.badRequest().body("Địa chỉ không hợp lệ.");
        }

        for (AddressDTO address : accountDTO.getAddresses()) {
            if (address.getAddressId() == null) {
                // Xử lý trường hợp không có ID, có thể tạo mới hoặc thông báo lỗi
                return ResponseEntity.badRequest().body("ID địa chỉ không hợp lệ.");
            }
        }

        // Tiến hành lưu hoặc cập nhật tài khoản và địa chỉ
        staffInformatiomService.saveAccountDetails(accountDTO);

        return ResponseEntity.ok(accountDTO);  // Trả về tài khoản đã được cập nhật
    }


    @PutMapping("/staff-change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            // Gọi service để thay đổi mật khẩu
            staffInformatiomService.changePassword(changePasswordDTO);
            return ResponseEntity.ok("Mật khẩu đã được thay đổi thành công!");
        } catch (Exception e) {
            // Trả về lỗi nếu có exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi khi thay đổi mật khẩu: " + e.getMessage());
        }
    }
}

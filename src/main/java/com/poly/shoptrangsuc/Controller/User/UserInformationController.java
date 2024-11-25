package com.poly.shoptrangsuc.Controller.User;

import com.poly.shoptrangsuc.DTO.AccountDTO;
import com.poly.shoptrangsuc.DTO.ChangePasswordDTO;
import com.poly.shoptrangsuc.Service.UserInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserInformationController {

    @Autowired
    private UserInformationService userInformationService;

    // Endpoint để lấy thông tin tài khoản của người dùng đang đăng nhập
    @GetMapping("/user-information")
    public ResponseEntity<AccountDTO> getAccountDetails(@RequestHeader("Authorization") String token) {
        try {
            // Loại bỏ "Bearer " nếu token có prefix
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Gọi service để lấy thông tin tài khoản
            AccountDTO accountDTO = userInformationService.getAccountDetails(token);

            // Trả về thông tin tài khoản dưới dạng ResponseEntity
            return ResponseEntity.ok(accountDTO);
        } catch (Exception e) {
            // Trả về lỗi nếu có exception (ví dụ: token không hợp lệ hoặc tài khoản không tồn tại)
            return ResponseEntity.status(400).body(null);
        }
    }



    @PostMapping("/save/user-information")
    public ResponseEntity<AccountDTO> saveAccountDetails(@RequestBody AccountDTO accountDTO) {
        try {
            AccountDTO savedAccount = userInformationService.saveAccountDetails(accountDTO);
            return ResponseEntity.ok(savedAccount);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null); // Trả về lỗi nếu có
        }
    }


    @PutMapping("/user-change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            // Gọi service để thay đổi mật khẩu
            userInformationService.changePassword(changePasswordDTO);
            return ResponseEntity.ok("Mật khẩu đã được thay đổi thành công!");
        } catch (Exception e) {
            // Trả về lỗi nếu có exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi khi thay đổi mật khẩu: " + e.getMessage());
        }
    }
}

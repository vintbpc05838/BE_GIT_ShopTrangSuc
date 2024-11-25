package com.poly.shoptrangsuc.Controller.User;


import com.poly.shoptrangsuc.DTO.AccountDTO;
import com.poly.shoptrangsuc.DTO.ContactMessage;
import com.poly.shoptrangsuc.Service.ContactService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contact")
    public ResponseEntity<AccountDTO> getAccountDetails(@RequestHeader("Authorization") String token) {
        try {
            // Loại bỏ "Bearer " nếu token có prefix
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Gọi service để lấy thông tin tài khoản
            AccountDTO accountDTO = contactService.getAccountDetails(token);

            // Trả về thông tin tài khoản dưới dạng ResponseEntity
            return ResponseEntity.ok(accountDTO);
        } catch (Exception e) {
            // Trả về lỗi nếu có exception (ví dụ: token không hợp lệ hoặc tài khoản không tồn tại)
            return ResponseEntity.status(400).body(null);
        }
    }


    @PostMapping("/contact/send")
    public ResponseEntity<String> sendContactMessage(@RequestBody ContactMessage contactMessage) {
        try {
            contactService.sendEmail(contactMessage);
            return ResponseEntity.ok("Thông tin liên hệ đã được gửi.");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Lỗi khi gửi email: " + e.getMessage());
        }
    }
}

package com.poly.shoptrangsuc.Controller.Admin;

import com.poly.shoptrangsuc.DTO.AccountDTO;
import com.poly.shoptrangsuc.DTO.AddressDTO;
import com.poly.shoptrangsuc.DTO.ChangePasswordDTO;
import com.poly.shoptrangsuc.Repository.AccountRepository;
import com.poly.shoptrangsuc.Repository.AddressRepository;
import com.poly.shoptrangsuc.Service.AdminInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminInformationController {

    @Autowired
    private AdminInformationService adminInformationService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AddressRepository addressRepository;

    // Endpoint để lấy thông tin tài khoản của người dùng đang đăng nhập
    @GetMapping("/admin-information")
    public ResponseEntity<AccountDTO> getAccountDetails(@RequestHeader("Authorization") String token) {
        try {
            // Loại bỏ "Bearer " nếu token có prefix
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Gọi service để lấy thông tin tài khoản
            AccountDTO accountDTO = adminInformationService.getAccountDetails(token);

            // Trả về thông tin tài khoản dưới dạng ResponseEntity
            return ResponseEntity.ok(accountDTO);
        } catch (Exception e) {
            // Trả về lỗi nếu có exception
            return ResponseEntity.status(400).body(null);
        }
    }



    @PutMapping("/save/admin-information")
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
        adminInformationService.updateAccount(accountDTO);

        return ResponseEntity.ok(accountDTO);  // Trả về tài khoản đã được cập nhật
    }




    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            // Gọi service để thay đổi mật khẩu
            adminInformationService.changePassword(changePasswordDTO);
            return ResponseEntity.ok("Mật khẩu đã được thay đổi thành công!");
        } catch (Exception e) {
            // Trả về lỗi nếu có exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Có lỗi khi thay đổi mật khẩu: " + e.getMessage());
        }
    }




    //xóa tài khoản
//    @DeleteMapping("/accounts/delete")
//    public ResponseEntity<?> deleteAccount() {
//        try {
//            // Lấy email của người dùng đang đăng nhập từ SecurityContext
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String loggedInEmail = authentication.getName();  // Email của người dùng đang đăng nhập
//
//            // Tìm tài khoản theo email của người dùng đang đăng nhập
//            Account account = accountRepository.findByEmail(loggedInEmail)
//                    .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
//
//            // Cập nhật trạng thái tài khoản thành 'vô hiệu hóa'
//            account.setStatus("inactive");  // Đổi trạng thái thành 'inactive'
//            accountRepository.save(account);  // Lưu lại tài khoản với trạng thái mới
//
//            // Xóa thông tin người dùng khỏi SecurityContext (logout)
//            SecurityContextHolder.clearContext();
//
//            return ResponseEntity.ok("Tài khoản đã được vô hiệu hóa.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi xử lý yêu cầu.");
//        }
//    }



//    @DeleteMapping("/accounts/delete/{accountId}")
//    public ResponseEntity<?> deleteAccount(@PathVariable("accountId") Integer accountId) {
//        try {
//            // Kiểm tra nếu accountId hợp lệ
//            Optional<Account> accountOptional = accountRepository.findById(accountId);
//            if (accountOptional.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản không tồn tại.");
//            }
//
//            // Cập nhật trạng thái tài khoản thành 'vô hiệu hóa'
//            Account account = accountOptional.get();
//            account.setStatus("inactive");
//            accountRepository.save(account);  // Lưu lại tài khoản đã cập nhật trạng thái
//
//            // Nếu tài khoản đang đăng nhập, thực hiện logout ngay sau khi vô hiệu hóa
//            SecurityContextHolder.clearContext();  // Xóa thông tin đăng nhập hiện tại
//
//            return ResponseEntity.ok("Tài khoản đã được vô hiệu hóa và đăng xuất.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra khi xử lý yêu cầu.");
//        }
//    }




}

package com.poly.shoptrangsuc.Controller.User;

import com.poly.shoptrangsuc.DTO.CheckoutRequest;
import com.poly.shoptrangsuc.DTO.UserInfoDTO;
import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Model.Address;
import com.poly.shoptrangsuc.Service.CheckoutService;
import com.poly.shoptrangsuc.Service.JWTService;
import com.poly.shoptrangsuc.Repository.AccountRepository;
import com.poly.shoptrangsuc.Repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AddressRepository addressRepository;

    @PostMapping("/process")
    public ResponseEntity<?> processCheckout(@RequestHeader("Authorization") String authHeader,
                                             @RequestBody CheckoutRequest request) {
        try {
            // Kiểm tra header Authorization
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Thiếu hoặc không hợp lệ Authorization header.");
            }

            // Lấy token từ Authorization header
            String token = authHeader.substring(7);

            // Kiểm tra tính hợp lệ của token
            if (jwtService.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token đã hết hạn.");
            }

            // Trích xuất email từ token
            String userEmail = jwtService.extractUsername(token);
            if (userEmail == null || userEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không thể xác thực người dùng.");
            }

            // Xác nhận tài khoản hợp lệ và chuyển email vào request
            request.setCustomerEmail(userEmail);

            // Gọi service xử lý đơn hàng và thanh toán
            String result = checkoutService.processCheckout(request);

            // Xử lý kết quả từ service
            if (result.startsWith("http")) {
                // Nếu VNPay trả về URL, gửi link để người dùng thanh toán
                return ResponseEntity.ok(result);
            } else if ("Đặt hàng thành công!".equals(result)) {
                // Nếu đặt hàng thành công
                return ResponseEntity.ok("Đặt hàng thành công! Đơn hàng của bạn đã được lưu.");
            }
        } catch (IllegalArgumentException e) {
            // Lỗi do dữ liệu không hợp lệ
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (RuntimeException e) {
            // Lỗi phát sinh trong quá trình xử lý logic
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi trong quá trình xử lý: " + e.getMessage());
        } catch (Exception e) {
            // Lỗi hệ thống không mong muốn
            e.printStackTrace(); // Ghi log lỗi chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi không xác định.");
        }
        return null;
    }






    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;

            if (token == null || jwtService.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ hoặc đã hết hạn.");
            }

            String userEmail = jwtService.extractUsername(token);
            if (userEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không lấy được thông tin người dùng từ token.");
            }

            Account account = accountRepository.findAccountByEmail(userEmail);
            if (account == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tài khoản không tồn tại.");
            }

            // Lấy địa chỉ mặc định của người dùng
            List<Address> defaultAddresses = addressRepository.findByIsDefaultTrue();
            if (defaultAddresses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy địa chỉ mặc định.");
            }

            // Lấy địa chỉ đầu tiên trong danh sách hoặc áp dụng logic tùy chỉnh
            Address defaultAddress = defaultAddresses.get(0);  // Changed index to 0 (fix potential out-of-bounds issue)

            UserInfoDTO userInfoDTO = new UserInfoDTO(
                    account.getFullname(),
                    account.getPhone(),
                    account.getEmail(),
                    defaultAddress.getAddress(),
                    defaultAddress.getCity(),
                    defaultAddress.getDistrict()
            );

            return ResponseEntity.ok(userInfoDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lấy thông tin người dùng: " + e.getMessage());
        }
    }
}

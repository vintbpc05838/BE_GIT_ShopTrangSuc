package com.poly.shoptrangsuc.GHN;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/shipping")
public class GHNController {

    private final GHNService ghnService;

    public GHNController(GHNService ghnService) {
        this.ghnService = ghnService;
    }

//    /**
//     * API tính phí vận chuyển dựa trên tài khoản người dùng
//     *
//     * @param accountId ID của tài khoản cần tính phí vận chuyển
//     * @return Phí vận chuyển dưới dạng BigDecimal hoặc lỗi nếu xảy ra
//     */
    @PostMapping("/fee")
    public ResponseEntity<?> getShippingFee(@RequestBody Map<String, Integer> request) {
        try {
            // Lấy accountId từ request body
            Integer accountId = request.get("accountId");
            if (accountId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Thiếu thông tin accountId trong request.");
            }

            // Tính phí vận chuyển
            BigDecimal fee = ghnService.calculateShippingFee(accountId);

            // Trả về kết quả
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("shippingFee", fee);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Xử lý lỗi do không tìm thấy dữ liệu hoặc lỗi từ GHN API
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            // Xử lý lỗi không mong muốn
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Lỗi hệ thống."));
        }
    }
}

package com.poly.shoptrangsuc.VNPAY;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
public class VnPayController {

    @Autowired
    private VnPayService vnPayService;

    @PostMapping("/pay")
    public ResponseEntity<String> pay(@RequestBody VNPayRequest vnPayRequest) {
        String paymentUrl = vnPayService.createOrder(vnPayRequest);
        return ResponseEntity.ok(paymentUrl);
    }

    @PostMapping("/return")
    public ResponseEntity<String> paymentReturn(HttpServletRequest request) {
        int result = vnPayService.orderReturn(request);
        if (result == 1) {
            return ResponseEntity.ok("Payment successful");
        } else if (result == 0) {
            return ResponseEntity.ok("Payment failed");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid response from VNPAY");
        }
    }
}

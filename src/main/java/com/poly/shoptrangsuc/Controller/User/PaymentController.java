package com.poly.shoptrangsuc.Controller.User;

import com.poly.shoptrangsuc.DTO.PaymentDTO;
import com.poly.shoptrangsuc.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO) {
        PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
        return ResponseEntity.ok(createdPayment);
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable Integer paymentId,
                                                    @RequestParam("amount") BigDecimal amount) {
        PaymentDTO updatedPayment = paymentService.updatePayment(paymentId, amount);
        return ResponseEntity.ok(updatedPayment);
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getPayments() {
        List<PaymentDTO> payments = paymentService.getPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByOrderId(@PathVariable Integer orderId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }
}

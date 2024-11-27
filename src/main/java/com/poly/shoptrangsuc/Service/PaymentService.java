package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.DTO.PaymentDTO;
import com.poly.shoptrangsuc.Model.Order;
import com.poly.shoptrangsuc.Model.Payment;
import com.poly.shoptrangsuc.Repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setOrder(new Order(paymentDTO.getOrderId())); // Thiết lập `Order` qua ID
        payment = paymentRepository.save(payment);
        return mapToDTO(payment);
    }

    public PaymentDTO updatePayment(Integer paymentId, BigDecimal amount) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thanh toán"));
        payment.setAmount(amount);
        payment = paymentRepository.save(payment);
        return mapToDTO(payment);
    }

    public List<PaymentDTO> getPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> getPaymentsByOrderId(Integer orderId) {
        return paymentRepository.findByOrderOrderId(orderId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PaymentDTO mapToDTO(Payment payment) {
        return new PaymentDTO(
                payment.getPaymentId(),
                payment.getAmount(),
                payment.getPaymentStatus(),
                payment.getPaymentDate(),
                payment.getPaymentMethod(),
                payment.getOrder().getOrderId()
        );
    }
}

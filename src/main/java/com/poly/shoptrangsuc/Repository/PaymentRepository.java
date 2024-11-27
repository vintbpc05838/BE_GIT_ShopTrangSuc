package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByOrderOrderId(Integer orderId);

    @Query("SELECT p FROM Payment p JOIN p.order o WHERE p.paymentId = :paymentId")
    public Optional<Payment> findUniquePayment(@Param("paymentId") Integer paymentId);
// Lấy danh sách thanh toán theo orderId
}

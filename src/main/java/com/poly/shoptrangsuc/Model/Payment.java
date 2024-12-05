package com.poly.shoptrangsuc.Model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Pay")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Integer paymentId;

    @Column(name = "Amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_date", nullable = false)  // Đảm bảo paymentDate không null
    private LocalDateTime paymentDate;

    @Column(name = "payment_method")
    private String paymentMethod;

    // Thêm quan hệ với Order
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)  // Cột này sẽ tham chiếu đến cột 'order_id' trong bảng Payment
    private Order order;

    // Constructor mặc định với giá trị paymentDate mặc định là ngày giờ hiện tại
    public Payment(String paymentMethod, BigDecimal amount, String paymentStatus, Order order) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = LocalDateTime.now();  // Khởi tạo paymentDate với ngày giờ hiện tại
        this.order = order;
    }

    // Constructor chỉ nhận paymentMethod
    public Payment(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now();  // Khởi tạo paymentDate với ngày giờ hiện tại
    }

    // Constructor chỉ nhận paymentId (dùng khi cần lấy đối tượng Payment từ ID)
    public Payment(Integer paymentId) {
        this.paymentId = paymentId;
        this.paymentDate = LocalDateTime.now();  // Khởi tạo paymentDate với ngày giờ hiện tại
    }

    // Nếu bạn muốn có constructor khác, hãy chắc chắn rằng paymentDate luôn được gán giá trị hợp lệ.
}

package com.poly.shoptrangsuc.Repository;
import com.poly.shoptrangsuc.Model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    OrderStatus findByStatusName(String statusName);
}

package com.poly.shoptrangsuc.Repository;
import com.poly.shoptrangsuc.Model.Order;
import com.poly.shoptrangsuc.Model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    OrderStatus findByStatusName(String statusName);
    @Query("SELECT o FROM OrderStatus o WHERE o.orderStatusId = :orderStatusId")
    Optional<OrderStatus> findByOrderStatusId(@Param("orderStatusId") Integer orderStatusId);

}

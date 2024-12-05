package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByOrderId(Integer orderId);
    List<Order> findByAccount(Account account);
    @Query("SELECT o FROM Order o WHERE o.orderStatus.orderStatusId = :statusId")
    List<Order> findByOrderStatusId(@Param("statusId") Integer statusId);
}
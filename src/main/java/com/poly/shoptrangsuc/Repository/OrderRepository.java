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
    List<Order> findByAccount_AccountId(Integer accountId);
    @Query("SELECT o FROM Order o WHERE o.orderStatus.orderStatusId = :statusId")
    List<Order> findByOrderStatusId(@Param("statusId") Integer statusId);
    @Query(value = "SELECT o.* FROM Orders o INNER JOIN Account a ON o.account_id = a.account_id WHERE a.fullname LIKE %:customerName%", nativeQuery = true)
    List<Order> findOrdersByCustomerName(@Param("customerName") String customerName);

}
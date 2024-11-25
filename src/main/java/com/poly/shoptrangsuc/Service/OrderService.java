package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.Model.Order;
import com.poly.shoptrangsuc.Model.OrderStatus;
import com.poly.shoptrangsuc.Repository.AccountRepository;
import com.poly.shoptrangsuc.Repository.OrderRepository;
import com.poly.shoptrangsuc.Repository.OrderStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService{
    private final OrderRepository orderRepository;


    @Autowired
    private OrderStatusService orderStatusService;

    public OrderService(OrderRepository orderRepository, AccountRepository accountRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> searchOrders(Integer orderId) {
        return orderRepository.findByOrderId(orderId);
    }
    public List<Order> getOrdersByStatus(Integer statusId) {
        return orderRepository.findByOrderStatusId(statusId);
    }
    public Integer findStatusIdByName(String statusName) {
        return orderStatusService.findIdByName(statusName); // Implement logic trong OrderStatusService
    }
    public Optional<Order> getOrderById(Integer orderId) {
        return orderRepository.findById(orderId); // Sử dụng findById từ JpaRepository
    }
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }
    public void updateOrder(Order order) {
        orderRepository.save(order); // Lưu thay đổi vào cơ sở dữ liệu
    }
    public void deleteOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }
    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Transactional
    public void updateOrderStatus(Integer orderId, Integer statusId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));

        OrderStatus newStatus = orderStatusRepository.findById(statusId)
                .orElseThrow(() -> new Exception("Invalid status ID"));

        order.setOrderStatus(newStatus);
        orderRepository.save(order);
    }
    public List<Order> findByAccountId(Integer accountId) {
        return orderRepository.findByAccount_AccountId(accountId);
    }
    public List<Order> searchOrdersByCustomerName(String customerName) {
        return orderRepository.findOrdersByCustomerName(customerName);
    }
}

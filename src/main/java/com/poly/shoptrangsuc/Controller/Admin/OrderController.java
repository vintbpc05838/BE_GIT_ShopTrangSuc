package com.poly.shoptrangsuc.Controller.Admin;

import com.poly.shoptrangsuc.DTO.OrderStatusUpdateRequest;
import com.poly.shoptrangsuc.Model.Order;
import com.poly.shoptrangsuc.Model.Payment;
import com.poly.shoptrangsuc.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/search") // Endpoint tìm kiếm
    public ResponseEntity<List<Order>> searchOrders(@RequestParam Integer orderId) {
        List<Order> orders = orderService.searchOrders(orderId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/filter") // Endpoint lọc theo trạng thái
    public ResponseEntity<List<Order>> getOrdersByStatus(@RequestParam Integer status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        if (order == null) {
            return ResponseEntity.badRequest().build();
        }
        Order savedOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Integer orderId, @RequestBody OrderStatusUpdateRequest request) {
        try {
            orderService.updateOrderStatus(orderId, request.getStatusId());
            return ResponseEntity.ok("Order status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update order status: " + e.getMessage());
        }
    }
    @GetMapping("/search-by-customer-name")  // New endpoint to search by customer name
    public ResponseEntity<List<Order>> searchOrdersByCustomerName(@RequestParam String customerName) {
        List<Order> orders = orderService.searchOrdersByCustomerName(customerName);
        return ResponseEntity.ok(orders);
    }
}

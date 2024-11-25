package com.poly.shoptrangsuc.Controller.User;

import com.poly.shoptrangsuc.DTO.OrderStatusUpdateRequest;
import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Model.Order;
import com.poly.shoptrangsuc.Model.OrderDetail;
import com.poly.shoptrangsuc.Service.AccountService;
import com.poly.shoptrangsuc.Service.JWTService;
import com.poly.shoptrangsuc.Service.OrderDetailService;
import com.poly.shoptrangsuc.Service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/order-history")
public class OrderHistoryController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    private final JWTService jwtService;
    private final AccountService accountService;

    public OrderHistoryController(JWTService jwtService, AccountService accountService) {
        this.jwtService = jwtService;
        this.accountService = accountService;
    }
    //@GetMapping("/account")
//public ResponseEntity<List<OrderDetailDTO>> getOrderDetailHistory(@RequestHeader("Authorization") String token) {
//    // Extract the username from the token (assuming Bearer token)
//    String username = jwtService.extractUsername(token.substring(7)); // Remove "Bearer " from token
//
//    // Find the account by email (username)
//    Account account = accountService.findByEmail(username)
//            .orElseThrow(() -> new IllegalArgumentException("Email not found"));
//
//    // Get the accountId
//    Integer accountId = account.getAccountId();
//
//    // Get order details by accountId
//    List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByAccountId(accountId);
//
//    // If no orders found, return 404
//    if (orderDetails.isEmpty()) {
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    // Convert OrderDetail to OrderDetailDTO
//    List<OrderDetailDTO> orderDetailDTOs = orderDetails.stream()
//            .map(orderDetail ->new OrderDetailDTO(
//                    orderDetail.getOrderDetailId(),
//                    orderDetail.getOrder().getOrderId(),
//                    orderDetail.getProduct().getProductId(),
//                    orderDetail.getQuantity(),
//                    orderDetail.getProduct().getProductName(),
//                    orderDetail.getProduct().getPrice()))
//            .collect(Collectors.toList());
//
//    return new ResponseEntity<>(orderDetailDTOs, HttpStatus.OK);
//    }
    @GetMapping("/account")
    public ResponseEntity<List<Order>> getOrderHistoryForAccount(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Remove "Bearer " from token
        String username = jwtService.extractUsername(token);

        // Tìm tài khoản theo email (username)
        Account account = accountService.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("Email not found"));

        // Lấy danh sách đơn hàng cho tài khoản
        List<Order> orders = orderService.findByAccountId(account.getAccountId());
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDetail>> getOrderDetailsByOrderId(@PathVariable Integer orderId) {
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
        if (orderDetails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }
    // Phương thức cập nhật trạng thái đơn hàng
    @PutMapping("/{orderId}/status") // Sử dụng PutMapping
    public ResponseEntity<String> updateOrderStatus(@PathVariable Integer orderId, @RequestBody OrderStatusUpdateRequest request) {
        try {
            orderService.updateOrderStatus(orderId, request.getStatusId());
            return ResponseEntity.ok("Order status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update order status: " + e.getMessage());
        }
    }
}

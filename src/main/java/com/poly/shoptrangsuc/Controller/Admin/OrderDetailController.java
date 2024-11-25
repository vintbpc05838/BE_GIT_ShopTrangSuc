package com.poly.shoptrangsuc.Controller.Admin;

import com.poly.shoptrangsuc.Model.OrderDetail;
import com.poly.shoptrangsuc.Service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/order-details")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping
    public ResponseEntity<List<OrderDetail>> getAllOrderDetails() {
        List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetails();
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<OrderDetail>> getOrderDetailsByStatus(@PathVariable Integer statusId) {
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByStatus(statusId);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDetail>> getOrderDetailsByOrderId(@PathVariable Integer orderId) {
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
        if (orderDetails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }
}

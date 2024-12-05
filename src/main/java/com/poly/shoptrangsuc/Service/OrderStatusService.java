package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.Model.OrderStatus;
import com.poly.shoptrangsuc.Repository.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusService {
    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public Integer findIdByName(String statusName) {
        OrderStatus status = orderStatusRepository.findByStatusName(statusName);
        return status != null ? status.getOrderStatusId() : null;
    }
}
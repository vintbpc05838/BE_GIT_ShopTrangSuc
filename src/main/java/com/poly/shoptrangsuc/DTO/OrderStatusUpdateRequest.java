package com.poly.shoptrangsuc.DTO;

import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    private Integer statusId;

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
}

package com.poly.shoptrangsuc.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RevenueDTO {

    private LocalDate ngay;
    private Integer tongHoaDon;
    private Double doanhThu;
}

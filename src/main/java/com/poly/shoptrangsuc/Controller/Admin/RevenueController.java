package com.poly.shoptrangsuc.Controller.Admin;

import com.poly.shoptrangsuc.DTO.RevenueDTO;
import com.poly.shoptrangsuc.Service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class RevenueController {
    @Autowired
    private RevenueService revenueService;

    @GetMapping("/revenue")
    public ResponseEntity<List<RevenueDTO>> getRevenue(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<RevenueDTO> revenue = revenueService.getRevenueByDateRange(startDate, endDate);
        return ResponseEntity.ok(revenue);
    }
}

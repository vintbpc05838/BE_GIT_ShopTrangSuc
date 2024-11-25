package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.DTO.RevenueDTO;
import com.poly.shoptrangsuc.Repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RevenueService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    // Phương thức thống kê doanh thu
    public List<RevenueDTO> getRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.findRevenueByDateRange(startDate, endDate);
    }
}

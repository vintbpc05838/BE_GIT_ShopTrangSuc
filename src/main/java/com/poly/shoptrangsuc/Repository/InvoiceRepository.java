package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.DTO.RevenueDTO;
import com.poly.shoptrangsuc.Model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    @Query("SELECT RevenueDTO(i.date, COUNT(i.id), SUM(i.total)) " +
            "FROM Invoice i WHERE i.date BETWEEN :startDate AND :endDate GROUP BY i.date ORDER BY i.date ASC")
    List<RevenueDTO> findRevenueByDateRange(LocalDate startDate, LocalDate endDate);
}

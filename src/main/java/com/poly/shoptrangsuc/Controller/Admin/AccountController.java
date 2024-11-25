package com.poly.shoptrangsuc.Controller.Admin;

import com.poly.shoptrangsuc.DTO.AccountDTO;
import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Service.AdminAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AccountController {

    @Autowired
    private AdminAccountService adminAccountService;

    // Lấy tất cả tài khoản
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = adminAccountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    // Lấy tất cả quản lý
    @GetMapping("/accounts/manager")
    public ResponseEntity<List<Account>> getAllManagers() {
        List<Account> managers = adminAccountService.getAllManagers();
        return ResponseEntity.ok(managers);
    }

    // Lấy tất cả nhân viên
    @GetMapping("/accounts/staff")
    public ResponseEntity<List<Account>> getAllStaff() {
        List<Account> staffs = adminAccountService.getAllStaff();
        return ResponseEntity.ok(staffs);
    }

    // Lấy tất cả khách hàng
    @GetMapping("/accounts/customer")
    public ResponseEntity<List<Account>> getAllCustomers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(adminAccountService.getAllCustomers());
    }

    // Lấy tài khoản theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Integer id) {
        Account account = adminAccountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    // Thêm mới quản lý
    @PostMapping("/accounts/managers")
    public ResponseEntity<Account> createManager(@RequestBody AccountDTO accountDTO) {
        accountDTO.setRole("ADMIN");
        Account createdManager = adminAccountService.createManagementAccount(accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdManager);
    }


    // Thêm mới nhân viên
    @PostMapping("/accounts/staffs")
    public ResponseEntity<Account> createStaff(@RequestBody AccountDTO accountDTO) {
        accountDTO.setRole("Nhân Viên");
        Account createdStaff = adminAccountService.createStaffAccount(accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStaff);
    }

    // Xóa tài khoản theo ID
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Integer id) {
        try {
            adminAccountService.deleteAccountById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Account deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

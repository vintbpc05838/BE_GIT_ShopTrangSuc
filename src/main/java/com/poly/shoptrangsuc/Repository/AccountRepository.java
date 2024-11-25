package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
 // Tìm kiếm theo tên đầy đủ
 Account findByFullname(String fullname);

 // Tìm kiếm theo email với Optional để tránh NullPointerException
 Optional<Account> findByEmail(String email);

 // Tìm kiếm tất cả tài khoản có vai trò nhất định
 List<Account> findByRole(Role role);

 // Kiểm tra sự tồn tại của tài khoản theo email
 boolean existsByEmail(String email);

 Account findAccountByEmail(String email);

}
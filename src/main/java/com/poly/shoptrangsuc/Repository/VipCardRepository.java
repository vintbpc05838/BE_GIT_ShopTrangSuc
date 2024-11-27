package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Model.VipCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VipCardRepository extends JpaRepository<VipCard, Integer> {

    // Tìm thẻ VIP theo tài khoản
    Optional<VipCard> findByAccount(Account account);
}

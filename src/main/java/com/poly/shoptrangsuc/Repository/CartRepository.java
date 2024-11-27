package com.poly.shoptrangsuc.Repository;




import com.poly.shoptrangsuc.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByAccount_AccountId(Integer accountId); // Lấy danh sách giỏ hàng theo ID tài khoản
    // Tìm giỏ hàng theo tài khoản và sản phẩm
    Optional<Cart> findByAccount_AccountIdAndProduct_ProductId(Integer accountId, Integer productId);
}



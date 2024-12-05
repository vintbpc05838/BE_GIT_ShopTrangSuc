package com.poly.shoptrangsuc.Controller.User;

import com.poly.shoptrangsuc.DTO.CartDTO;
import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Service.AccountService;
import com.poly.shoptrangsuc.Service.AuthenticationService;
import com.poly.shoptrangsuc.Service.CartService;
import com.poly.shoptrangsuc.Service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    private final CartService cartService;
    private final JWTService jwtService;
    private final AccountService accountService;

    @Autowired
    public CartController(CartService cartService, JWTService jwtService, AccountService accountService) {
        this.cartService = cartService;
        this.jwtService = jwtService;
        this.accountService = accountService;
    }

    // Lấy danh sách giỏ hàng của tài khoản từ token
    @GetMapping
    public ResponseEntity<List<CartDTO>> getCartsByToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Lấy token từ header
        String username = jwtService.extractUsername(token); // Lấy username từ token

        // Tìm tài khoản dựa trên email
        Account account = accountService.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        // Lấy giỏ hàng của tài khoản
        List<CartDTO> carts = cartService.getCartsByAccountId(account.getAccountId());
        return ResponseEntity.ok(carts); // Trả về danh sách giỏ hàng dưới dạng CartDTO
    }

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add")
    public ResponseEntity<CartDTO> addCart(HttpServletRequest request, @RequestBody CartDTO cartDTO) {
        String token = request.getHeader("Authorization").substring(7); // Lấy token từ header
        String username = jwtService.extractUsername(token); // Lấy username từ token

        // Lấy thông tin tài khoản từ token
        Account account = accountService.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        cartDTO.setAccountId(account.getAccountId()); // Gán accountId từ token vào DTO
        CartDTO addedCart = cartService.addCart(cartDTO);
        return ResponseEntity.ok(addedCart); // Trả về giỏ hàng đã thêm hoặc đã cập nhật
    }

    // Xóa sản phẩm trong giỏ hàng
    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Integer cartId) {
        cartService.deleteCartById(cartId);
        return ResponseEntity.noContent().build(); // Trả về mã trạng thái No Content (204)
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @PutMapping("/update/{cartId}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable Integer cartId, @RequestParam Integer quantity) {
        Optional<CartDTO> updatedCart = cartService.updateCart(cartId, quantity);
        return updatedCart.map(ResponseEntity::ok) // Trả về giỏ hàng đã cập nhật
                .orElseGet(() -> ResponseEntity.notFound().build()); // Nếu không tìm thấy giỏ hàng thì trả về Not Found
    }
}

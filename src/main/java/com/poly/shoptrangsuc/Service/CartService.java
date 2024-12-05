package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.DTO.CartDTO;
import com.poly.shoptrangsuc.Model.Cart;
import com.poly.shoptrangsuc.Model.ProductDetails;
import com.poly.shoptrangsuc.Repository.AccountRepository;
import com.poly.shoptrangsuc.Repository.CartRepository;
import com.poly.shoptrangsuc.Repository.ProductDetailsRepository;
import com.poly.shoptrangsuc.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final ProductDetailsRepository productDetailsRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository,
                       AccountRepository accountRepository, ProductDetailsRepository productDetailsRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
        this.productDetailsRepository = productDetailsRepository;
    }

    // Lấy danh sách giỏ hàng theo accountId
    public List<CartDTO> getCartsByAccountId(Integer accountId) {
        return cartRepository.findByAccount_AccountId(accountId)
                .stream()
                .map(this::toCartDTO)
                .collect(Collectors.toList());
    }

    // Thêm sản phẩm vào giỏ hàng
    @Transactional
    public CartDTO addCart(CartDTO cartDTO) {
        Optional<Cart> existingCartOpt = cartRepository.findByAccount_AccountIdAndProduct_ProductId(cartDTO.getAccountId(), cartDTO.getProductId());

        if (existingCartOpt.isPresent()) {
            // Nếu sản phẩm đã có trong giỏ hàng, chỉ cần cập nhật số lượng
            Cart existingCart = existingCartOpt.get();
            existingCart.setQuantity(existingCart.getQuantity() + cartDTO.getQuantity()); // Cộng thêm số lượng
            return toCartDTO(cartRepository.save(existingCart)); // Lưu lại giỏ hàng đã được cập nhật
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, thêm sản phẩm mới
            Optional<ProductDetails> productDetailsOpt = productDetailsRepository.findById(cartDTO.getProductId());

            if (productDetailsOpt.isPresent()) {
                ProductDetails productDetails = productDetailsOpt.get();

                Cart cart = new Cart();
                cart.setPrice(productDetails.getPrice());
                cart.setQuantity(cartDTO.getQuantity());

                productRepository.findById(cartDTO.getProductId()).ifPresent(cart::setProduct);
                accountRepository.findById(cartDTO.getAccountId()).ifPresent(cart::setAccount);

                return toCartDTO(cartRepository.save(cart));
            }
        }
        return null;
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @Transactional
    public void deleteCartById(Integer cartId) {
        cartRepository.deleteById(cartId);
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @Transactional
    public Optional<CartDTO> updateCart(Integer cartId, Integer quantity) {
        Optional<Cart> existingCart = cartRepository.findById(cartId);
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(quantity);
            return Optional.of(toCartDTO(cartRepository.save(cart)));
        }
        return Optional.empty();
    }

    // Chuyển đổi từ Cart sang CartDTO
    private CartDTO toCartDTO(Cart cart) {
        String sizeDescription = null;
        String productName = null;
        String productCategory = null;
        String productImage = null;

        if (cart.getProduct() != null) {
            productName = cart.getProduct().getProductName(); // Lấy tên sản phẩm

            if (cart.getProduct().getProductDetails() != null) {
                ProductDetails productDetails = cart.getProduct().getProductDetails();

                if (productDetails.getSize() != null) {
                    sizeDescription = String.valueOf(productDetails.getSize().getSizeDescription()); // Lấy mô tả kích thước
                }
                if (productDetails.getProductCategory() != null) {
                    productCategory = productDetails.getProductCategory().getCategoryName(); // Lấy loại sản phẩm
                }
                if (productDetails.getImages() != null) {
                    productImage = String.valueOf(productDetails.getImages()); // Lấy hình ảnh sản phẩm
                }
            }
        }

        return new CartDTO(
                cart.getCartId(),
                cart.getPrice(),
                cart.getQuantity(),
                cart.getProduct().getProductId(),
                cart.getAccount().getAccountId(),
                sizeDescription,  // Mô tả kích thước
                productName,      // Tên sản phẩm
                productCategory,  // Loại sản phẩm
                productImage      // Hình ảnh sản phẩm
        );
    }

    // Hàm xóa sản phẩm khỏi giỏ hàng sau khi đã tạo đơn hàng
    @Transactional
    public void removeProductsFromCartAfterOrder(Integer accountId, List<Integer> productIds) {
        List<Cart> cartsToRemove = cartRepository.findByAccount_AccountId(accountId)
                .stream()
                .filter(cart -> productIds.contains(cart.getProduct().getProductId()))
                .collect(Collectors.toList());

        for (Cart cart : cartsToRemove) {
            cartRepository.delete(cart); // Xóa sản phẩm khỏi giỏ hàng
        }
    }

    // Hàm tạo đơn hàng và xóa sản phẩm khỏi giỏ hàng
    @Transactional
    public void createOrderAndRemoveFromCart(Integer accountId, List<Integer> productIds) {
        // Logic tạo đơn hàng (thêm logic tạo đơn hàng ở đây)

        // Sau khi tạo đơn hàng thành công, xóa sản phẩm khỏi giỏ hàng
        removeProductsFromCartAfterOrder(accountId, productIds);
    }
}

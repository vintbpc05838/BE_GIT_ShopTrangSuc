package com.poly.shoptrangsuc.DTO;

import java.math.BigDecimal;

public class CartDTO {
    private Integer cartId;
    private BigDecimal price;
    private Integer quantity;
    private Integer productId;
    private Integer accountId;
    private String size;  // Mô tả kích thước
    private String productName;  // Tên sản phẩm
    private String productCategory;  // Loại sản phẩm
    private String productImage;  // Hình ảnh sản phẩm

    // Constructors
    public CartDTO() {}

    public CartDTO(Integer cartId, BigDecimal price, Integer quantity, Integer productId, Integer accountId,
                   String size, String productName, String productCategory, String productImage) {
        this.cartId = cartId;
        this.price = price;
        this.quantity = quantity;
        this.productId = productId;
        this.accountId = accountId;
        this.size = size;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productImage = productImage; // Thêm hình ảnh sản phẩm
    }

    // Getters and setters
    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage; // Thêm setter cho hình ảnh sản phẩm
    }
}

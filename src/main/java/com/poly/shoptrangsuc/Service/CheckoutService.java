package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.DTO.CheckoutRequest;
import com.poly.shoptrangsuc.GHN.GHNService;
import com.poly.shoptrangsuc.Model.*;
import com.poly.shoptrangsuc.Repository.*;
import com.poly.shoptrangsuc.VNPAY.VnPayService;
import com.poly.shoptrangsuc.VNPAY.VNPayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private GHNService ghnService;

    @Autowired
    private VnPayService vnPayService;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private ProductRepository productRepository;

    // Phương thức xử lý đơn hàng
    public String processCheckout(CheckoutRequest request) {
        try {
            // Lấy tài khoản người dùng từ email
            Account userAccount = getUserAccount(request.getCustomerEmail());

            // Lấy địa chỉ mặc định của người dùng
            Address defaultAddress = getDefaultAddress(userAccount);

            // Kiểm tra danh sách sản phẩm
            validateProducts(request);

            // Tính phí vận chuyển
            BigDecimal shippingFee = calculateShippingFee(request.getShippingMethod(), defaultAddress);

            // Tính tổng giá trị đơn hàng
            BigDecimal subtotal = calculateSubtotal(request);
            BigDecimal totalAmount = subtotal.add(shippingFee);

            // Tạo và lưu đơn hàng
            Order savedOrder = createOrder(request, userAccount, defaultAddress, shippingFee, totalAmount, subtotal);

            // Xử lý thanh toán
            String paymentUrl = processPayment(request, savedOrder, totalAmount);

            // Lưu chi tiết đơn hàng
            saveOrderDetails(request, savedOrder);

            // Xóa sản phẩm khỏi giỏ hàng sau khi tạo đơn
            cartService.createOrderAndRemoveFromCart(userAccount.getAccountId(), getProductIds(request));

            // Trả kết quả
            return paymentUrl != null ? paymentUrl : "Đặt hàng thành công, thanh toán bằng COD.";
        } catch (Exception e) {
            // Ghi lại lỗi và ném ra ngoại lệ cho phản hồi từ client
            System.err.println("Lỗi trong quá trình thanh toán: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi trong quá trình xử lý đơn hàng: " + e.getMessage());
        }
    }

    // Lấy tài khoản người dùng theo email
    private Account getUserAccount(String email) {
        return accountService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản không tồn tại."));
    }

    // Lấy địa chỉ mặc định của người dùng
    private Address getDefaultAddress(Account userAccount) {
        return userAccount.getAddresses().stream()
                .filter(Address::getIsDefault)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ mặc định."));
    }

    // Kiểm tra xem danh sách sản phẩm có hợp lệ không
    private void validateProducts(CheckoutRequest request) {
        if (request.getProducts() == null || request.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Danh sách sản phẩm không được để trống.");
        }
    }

    // Tính phí vận chuyển
    private BigDecimal calculateShippingFee(String shippingMethod, Address defaultAddress) {
        if ("DELIVERY".equalsIgnoreCase(shippingMethod)) {
            String districtId = defaultAddress.getDistrict();
            String wardCode = defaultAddress.getWard(); // Đảm bảo mã phường được gán chính xác
            // Bỏ comment dòng sau khi đã triển khai dịch vụ GHN
            // return ghnService.calculateShippingFee(districtId, wardCode);
            return BigDecimal.ZERO; // Placeholder cho phí vận chuyển thực tế
        } else if ("PICKUP".equalsIgnoreCase(shippingMethod)) {
            return BigDecimal.ZERO;
        } else {
            throw new IllegalArgumentException("Phương thức vận chuyển không hợp lệ.");
        }
    }

    // Tính tổng giá trị đơn hàng
    private BigDecimal calculateSubtotal(CheckoutRequest request) {
        return request.getProducts().stream()
                .map(p -> p.getPrice().multiply(new BigDecimal(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Tạo đơn hàng mới và lưu vào cơ sở dữ liệu
    private Order createOrder(CheckoutRequest request, Account userAccount, Address defaultAddress, BigDecimal shippingFee, BigDecimal totalAmount, BigDecimal subtotal) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setTotatAmount(subtotal);
        order.setPromotionalDiscount(0); // Điều chỉnh nếu có khuyến mãi trong ứng dụng
        order.setVipDiscount(0); // Điều chỉnh nếu cần
        order.setAddress(defaultAddress.getAddress());
        order.setTotatAmountAfterDiscount(totalAmount);
        order.setAccount(userAccount);

        // Cập nhật phương thức vận chuyển dựa trên thông tin đầu vào
        order.setShippingMethod(new ShippingMethod("DELIVERY".equalsIgnoreCase(request.getShippingMethod()) ? 1 : 2));

        // Lấy trạng thái đơn hàng mặc định
        OrderStatus defaultStatus = orderStatusRepository.findByOrderStatusId(1)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái đơn hàng mặc định."));
        order.setOrderStatus(defaultStatus);

        return orderRepository.save(order);
    }

    // Xử lý thanh toán
    private String processPayment(CheckoutRequest request, Order savedOrder, BigDecimal totalAmount) {
        Payment payment = new Payment();
        payment.setAmount(totalAmount);
        payment.setPaymentStatus("Pending");
        payment.setOrder(savedOrder);
        payment.setPaymentDate(LocalDateTime.now());

        String paymentUrl = null;
        if ("VNPAY".equalsIgnoreCase(request.getPaymentMethod())) {
            payment.setPaymentMethod("VNPAY");
            // Cấu hình yêu cầu VNPay
            VNPayRequest vnPayRequest = new VNPayRequest();
            vnPayRequest.setTotal(totalAmount.intValue());
            vnPayRequest.setOrderInfo("Thanh toán cho đơn hàng #" + savedOrder.getOrderId());
            vnPayRequest.setReturnUrl(request.getReturnUrl());

            paymentUrl = vnPayService.createOrder(vnPayRequest); // Sử dụng VnPayService để lấy URL thanh toán
        } else if ("COD".equalsIgnoreCase(request.getPaymentMethod())) {
            payment.setPaymentMethod("COD"); // Xử lý thanh toán khi giao hàng
        } else {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ.");
        }

        Payment savedPayment = paymentRepository.save(payment); // Lưu thông tin thanh toán
        savedOrder.setPayment(savedPayment); // Liên kết thanh toán với đơn hàng
        orderRepository.save(savedOrder); // Lưu đơn hàng với thanh toán

        return paymentUrl; // Trả về URL thanh toán hoặc null nếu là COD
    }

    // Lưu các chi tiết đơn hàng
    private void saveOrderDetails(CheckoutRequest request, Order savedOrder) {
        request.getProducts().forEach(product -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(savedOrder);
            Product existingProduct = productRepository.findById(product.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại với ID: " + product.getProductId()));
            orderDetail.setProduct(existingProduct);
            orderDetail.setQuantity(product.getQuantity());
            orderDetailRepository.save(orderDetail); // Lưu chi tiết đơn hàng
        });
    }

    // Phương thức lấy danh sách productIds từ yêu cầu
    private List<Integer> getProductIds(CheckoutRequest request) {
        return request.getProducts().stream()
                .map(product -> product.getProductId())
                .collect(Collectors.toList());
    }
}

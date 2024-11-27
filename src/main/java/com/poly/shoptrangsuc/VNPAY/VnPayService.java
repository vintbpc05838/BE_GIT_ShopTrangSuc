package com.poly.shoptrangsuc.VNPAY;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VnPayService {

    private static final Logger logger = LoggerFactory.getLogger(VnPayService.class);

    @Value("${vnp.version:2.1.0}")
    private String vnpVersion;

    @Value("${vnp.command:pay}")
    private String vnpCommand;

    @Value("${vnp.locale:vn}")
    private String vnpLocale;

    @Value("${vnp.orderType:order-type}")
    private String orderType;

    public String createOrder(VNPayRequest vnPayRequest) {
        String vnpTxnRef = VnPayConfig.getRandomNumber(8);
        String vnpIpAddr = "127.0.0.1";
        String vnpTmnCode = VnPayConfig.vnpTmnCode;

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpVersion);
        vnpParams.put("vnp_Command", vnpCommand);
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(vnPayRequest.getTotal() * 100));  // Tổng tiền tính bằng đơn vị tiền tệ
        vnpParams.put("vnp_CurrCode", "VND");

        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderInfo", vnPayRequest.getOrderInfo());
        vnpParams.put("vnp_OrderType", orderType);

        vnpParams.put("vnp_Locale", vnpLocale);
        vnpParams.put("vnp_ReturnUrl", vnPayRequest.getReturnUrl() + VnPayConfig.vnpReturnUrl);
        vnpParams.put("vnp_IpAddr", vnpIpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                try {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                            .append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                        query.append('&');
                        hashData.append('&');
                    }
                } catch (UnsupportedEncodingException e) {
                    logger.error("Error encoding URL parameters", e);
                }
            }
        }

        String vnpSecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnpHashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnpSecureHash);

        return VnPayConfig.vnpPayUrl + "?" + query.toString();
    }

    public int orderReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();

        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            try {
                fieldName = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                logger.error("Error encoding URL parameters", e);
            }
            if (fieldValue != null && !fieldValue.isEmpty()) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnpSecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        String signValue = VnPayConfig.hashAllFields(fields);
        if (signValue.equals(vnpSecureHash)) {
            return "00".equals(request.getParameter("vnp_TransactionStatus")) ? 1 : 0;
        } else {
            return -1;
        }
    }
}

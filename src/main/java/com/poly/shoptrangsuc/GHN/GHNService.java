package com.poly.shoptrangsuc.GHN;

import com.poly.shoptrangsuc.Model.Address;
import com.poly.shoptrangsuc.Repository.AddressRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class GHNService {

    @Value("${ghn.token}")
    private String ghnToken;

    @Value("${ghn.api.url}")
    private String ghnApiUrl;

    private final AddressRepository addressRepository;

    public GHNService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public BigDecimal calculateShippingFee(Integer accountId) {
        try {
            Address address = getValidAddress(accountId);
            Long provinceId = getProvinceIdFromAPI(address.getCity());
            Long districtId = getDistrictIdFromAPI(address.getDistrict(), provinceId);
            String wardCode = getWardCodeFromAPI(districtId, address.getWard());

            Map<String, Object> serviceInfo = getServiceInfoFromAPI(provinceId, districtId);
            Integer serviceId = (Integer) serviceInfo.get("service_id");
            Integer serviceTypeId = (Integer) serviceInfo.get("service_type_id");

            Map<String, Object> params = prepareShippingParams(districtId, wardCode, serviceId, serviceTypeId);
            System.out.println("Request Parameters to GHN API: " + params); // Log request parameters

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = createHeaders();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    ghnApiUrl + "/shiip/public-api/v2/shipping-order/fee",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            System.out.println("Response from GHN API: " + response.getStatusCode()); // Log response status
            System.out.println("Response Body: " + response.getBody()); // Log response body

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                return new BigDecimal(data.get("total").toString());
            }

            throw new RuntimeException("Không thể tính phí vận chuyển. Lỗi từ API GHN: " + response.getStatusCode());
        } catch (HttpClientErrorException e) {
            // Handle the specific case of a 400 error
            System.err.println("Bad Request: " + e.getResponseBodyAsString());
            throw new RuntimeException("Lỗi khi tính phí vận chuyển: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tính phí vận chuyển: " + e.getMessage(), e);
        }
    }

    private Address getValidAddress(Integer accountId) {
        return addressRepository.findByIsDefaultTrue()
                .stream()
                .filter(addr -> addr.getAccount().getAccountId().equals(accountId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ mặc định"));
    }

    private Long getProvinceIdFromAPI(String city) {
        if (city == null || city.isEmpty()) {
            throw new RuntimeException("Tên tỉnh/thành phố không hợp lệ: " + city);
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = createHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    ghnApiUrl + "/shiip/public-api/master-data/province",
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                for (Map<String, Object> province : (Iterable<Map<String, Object>>) responseBody.get("data")) {
                    if (city.equalsIgnoreCase(province.get("ProvinceName").toString())) {
                        return Long.parseLong(province.get("ProvinceID").toString());
                    }
                }
            }

            throw new RuntimeException("Không tìm thấy tỉnh/thành phố: " + city);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gọi API GHN để lấy mã tỉnh/thành phố: " + e.getMessage(), e);
        }
    }

    private Long getDistrictIdFromAPI(String districtName, Long provinceId) {
        if (districtName == null || districtName.isEmpty()) {
            throw new RuntimeException("Tên quận huyện không hợp lệ: " + districtName);
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = createHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    ghnApiUrl + "/shiip/public-api/master-data/district",
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                for (Map<String, Object> district : (Iterable<Map<String, Object>>) responseBody.get("data")) {
                    if (districtName.equalsIgnoreCase(district.get("DistrictName").toString())
                            && district.get("ProvinceID").equals(provinceId)) {
                        return Long.parseLong(district.get("DistrictID").toString());
                    }
                }
            }

            throw new RuntimeException("Không tìm thấy quận huyện: " + districtName);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gọi API GHN để lấy mã quận huyện: " + e.getMessage(), e);
        }
    }

    private String getWardCodeFromAPI(Long districtId, String wardName) {
        if (wardName == null || wardName.isEmpty()) {
            throw new RuntimeException("Tên phường không hợp lệ: " + wardName);
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = createHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    ghnApiUrl + "/shiip/public-api/master-data/ward?district_id=" + districtId,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                for (Map<String, Object> ward : (Iterable<Map<String, Object>>) responseBody.get("data")) {
                    if (wardName.equalsIgnoreCase(ward.get("WardName").toString())) {
                        return ward.get("WardCode").toString();
                    }
                }
            }

            throw new RuntimeException("Không tìm thấy phường: " + wardName);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gọi API GHN để lấy mã phường: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> getServiceInfoFromAPI(Long provinceId, Long districtId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = createHeaders();
            Map<String, Object> params = new HashMap<>();
            params.put("from_district_id", 1442); // Thay thế bằng ID quận của người gửi
            params.put("to_district_id", districtId);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    ghnApiUrl + "/shiip/public-api/v2/shipping-order/available-services",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                for (Map<String, Object> service : (Iterable<Map<String, Object>>) responseBody.get("data")) {
                    return service; // Lấy service đầu tiên trong danh sách
                }

                throw new RuntimeException("Không tìm thấy dịch vụ vận chuyển phù hợp.");
            }

            throw new RuntimeException("API GHN không trả về danh sách dịch vụ vận chuyển hợp lệ.");
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gọi API GHN để lấy thông tin dịch vụ: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> prepareShippingParams(Long districtId, String wardCode, Integer serviceId, Integer serviceTypeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("from_district_id", 1442); // ID quận của người gửi, thay thế bằng ID đúng
        params.put("to_district_id", districtId);
        params.put("to_ward_code", wardCode);
        params.put("height", 10); // Độ cao (cm)
        params.put("length", 10); // Độ dài (cm)
        params.put("weight", 1000); // Trọng lượng đơn hàng (gram)
        params.put("width", 10); // Độ rộng (cm)
        params.put("insurance_value", 100000); // Giá trị bảo hiểm
        params.put("service_id", serviceId); // Thêm service_id
        params.put("service_type_id", serviceTypeId); // Thêm service_type_id
        return params;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", ghnToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
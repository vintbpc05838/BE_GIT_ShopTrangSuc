package com.poly.shoptrangsuc.Controller.User;

import com.poly.shoptrangsuc.DTO.AccountDTO;
import com.poly.shoptrangsuc.DTO.AddressDTO;
import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Model.Address;
import com.poly.shoptrangsuc.Repository.AccountRepository;
import com.poly.shoptrangsuc.Repository.AddressRepository;
import com.poly.shoptrangsuc.Service.AddressService;
import com.poly.shoptrangsuc.Service.Impl.JWTServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private JWTServiceImpl jwtService;

    // Thêm địa chỉ
    @PostMapping("/address/add")
    public ResponseEntity<?> addAddress(@RequestBody Address address, @RequestHeader("Authorization") String token) {
        // Kiểm tra tính hợp lệ của token
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không có token hoặc token không hợp lệ");
        }

        String username = jwtService.extractUsername(token.substring(7));  // Lấy username từ token
        Optional<Account> accountOptional = accountRepository.findByEmail(username);

        if (!accountOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản không hợp lệ");
        }

        Account account = accountOptional.get();
        address.setAccount(account);

        // Kiểm tra nếu isDefault là null thì gán mặc định là false
        if (address.getIsDefault() == null) {
            address.setIsDefault(false);
        }

        addressService.save(address); // Lưu địa chỉ
        return ResponseEntity.ok("Địa chỉ đã được thêm thành công!");
    }

    // Lấy danh sách địa chỉ của người dùng hiện tại
    @GetMapping("/address/list")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        try {
            // Gọi service để lấy danh sách địa chỉ
            List<AddressDTO> addresses = addressService.getAddressesByUserEmail();
            if (addresses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(addresses);
            }
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Log lỗi để hỗ trợ debug
            System.out.println("Lỗi khi lấy danh sách địa chỉ: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cập nhật địa chỉ
    @PutMapping("/address/update/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Integer id, @RequestBody Address address, @RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không có token hoặc token không hợp lệ");
        }

        String username = jwtService.extractUsername(token.substring(7));
        Optional<Account> accountOptional = accountRepository.findByEmail(username);

        if (!accountOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản không hợp lệ");
        }

        Account account = accountOptional.get();
        Optional<Address> existingAddress = addressRepository.findById(id);
        if (existingAddress.isPresent()) {
            Address addressToUpdate = existingAddress.get();
            addressToUpdate.setAddress(address.getAddress());
            addressToUpdate.setCity(address.getCity());
            addressToUpdate.setDistrict(address.getDistrict());
            addressToUpdate.setIsDefault(address.getIsDefault() != null ? address.getIsDefault() : false); // Xử lý isDefault

            addressToUpdate.setAccount(account);
            addressService.save(addressToUpdate);
            return ResponseEntity.ok("Địa chỉ đã được cập nhật thành công!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Địa chỉ không tồn tại");
    }

    // Xóa địa chỉ
    @DeleteMapping("/address/delete/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Integer id) {
        // Kiểm tra nếu id là null hoặc không hợp lệ
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("ID địa chỉ không hợp lệ.");
        }

        try {
            addressService.deleteAddressById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Address deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    // Lấy thông tin tài khoản và địa chỉ của người dùng hiện tại
    @GetMapping("/account")
    public ResponseEntity<AccountDTO> getAccount() {
        try {
            AccountDTO accountDTO = addressService.getAccountByEmail();
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }



//    @PutMapping("/address/set-default/{addressId}/{accountId}")
//    public ResponseEntity<?> setDefaultAddress(@PathVariable("addressId") Integer addressId, @PathVariable("accountId") Integer accountId) {
//        // Kiểm tra xem có nhận được addressId và accountId không
//        if (addressId == null || accountId == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing addressId or accountId");
//        }
//
//        try {
//            boolean result = addressService.setDefaultAddress(addressId, accountId);
//            if (result) {
//                return ResponseEntity.ok("Đã đặt địa chỉ mặc định thành công!");
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy địa chỉ hoặc tài khoản.");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
//        }
//    }


    @PutMapping("/address/setDefault/{addressId}")
    public ResponseEntity<?> setDefaultAddress(
            @PathVariable Integer addressId,
            @RequestHeader("Authorization") String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Token không hợp lệ.\"}");
        }

        String token = authorizationHeader.substring(7); // Lấy phần token từ Bearer token

        try {
            // Giải mã token và lấy email
            String email = jwtService.extractEmailFromToken(token);
            if (email == null || email.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":\"Token không chứa email.\"}");
            }

            Address defaultAddress = addressService.setDefaultAddress(addressId, email);
            return ResponseEntity.ok(defaultAddress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\":\"Không thể đặt địa chỉ mặc định. Lý do: " + e.getMessage() + "\"}");
        }
    }









}

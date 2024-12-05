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
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ");
        }

        String username = jwtService.extractUsername(token.substring(7));  // Lấy username từ token
        Optional<Account> accountOptional = accountRepository.findByEmail(username);

        if (!accountOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Tài khoản không hợp lệ");
        }

        Account account = accountOptional.get();
        address.setAccount(account);

        if (address.getIsDefault() == null) {
            address.setIsDefault(false); // Gán mặc định nếu isDefault là null
        }

        addressService.save(address); // Lưu địa chỉ
        return ResponseEntity.ok("Địa chỉ đã được thêm thành công!");
    }

    // Lấy danh sách địa chỉ của người dùng hiện tại
    @GetMapping("/address/list")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        try {
            List<AddressDTO> addresses = addressService.getAddressesByUserEmail();
            if (addresses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(addresses);
            }
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cập nhật địa chỉ
    @PutMapping("/address/update/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Integer id, @RequestBody Address address, @RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ");
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
            addressToUpdate.setWard(address.getWard()); // Cập nhật ward
            addressToUpdate.setIsDefault(address.getIsDefault() != null ? address.getIsDefault() : false);

            addressToUpdate.setAccount(account);
            addressService.save(addressToUpdate);
            return ResponseEntity.ok("Địa chỉ đã được cập nhật thành công!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Địa chỉ không tồn tại");
    }

    // Xóa địa chỉ
    @DeleteMapping("/address/delete/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("ID địa chỉ không hợp lệ.");
        }

        try {
            addressService.deleteAddressById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Address deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address not found.");
        }
    }
}

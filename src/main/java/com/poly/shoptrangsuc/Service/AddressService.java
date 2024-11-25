package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.DTO.AccountDTO;
import com.poly.shoptrangsuc.DTO.AddressDTO;
import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Model.Address;
import com.poly.shoptrangsuc.Repository.AccountRepository;
import com.poly.shoptrangsuc.Repository.AddressRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Thêm địa chỉ
    public Address save(Address address) {
        // Kiểm tra nếu giá trị isDefault là null thì gán mặc định là false
        if (address.getIsDefault() == null) {
            address.setIsDefault(false);
        }
        return addressRepository.save(address);
    }

    // Lấy danh sách địa chỉ của người dùng qua email
    public List<AddressDTO> getAddressesByUserEmail() {
        // Lấy email của người dùng từ SecurityContext
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Tìm tài khoản qua email
        Optional<Account> accountOptional = accountRepository.findByEmail(email);
        if (!accountOptional.isPresent()) {
            throw new RuntimeException("Account not found");
        }

        Account account = accountOptional.get();

        // Lấy danh sách địa chỉ của tài khoản hiện tại
        List<Address> addresses = addressRepository.findByAccount(account);

        // Chuyển đổi từ List<Address> sang List<AddressDTO>
        return addresses.stream()
                .map(address -> new AddressDTO(
                        address.getAddressId(),
                        address.getAddress(),
                        address.getCity(),
                        address.getDistrict(),
                        address.getIsDefault() != null ? address.getIsDefault() : false))  // Kiểm tra null và gán mặc định nếu null
                .collect(Collectors.toList());
    }

    // Cập nhật địa chỉ
    public AddressDTO updateAddress(Integer id, AddressDTO updatedAddressDTO) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setAddress(updatedAddressDTO.getAddress());
        address.setCity(updatedAddressDTO.getCity());
        address.setDistrict(updatedAddressDTO.getDistrict());

        // Kiểm tra null khi cập nhật isDefault, gán false nếu null
        address.setIsDefault(updatedAddressDTO.getIsDefault() != null ? updatedAddressDTO.getIsDefault() : false);

        Address updatedAddress = addressRepository.save(address);
        return new AddressDTO(updatedAddress.getAddressId(),updatedAddress.getAddress(), updatedAddress.getCity(), updatedAddress.getDistrict(), updatedAddress.getIsDefault());
    }

    // Xóa địa chỉ
    public void deleteAddressById(Integer id) {
        System.out.println("Attempting to delete address with ID: " + id);
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            System.out.println("Address with ID " + id + " deleted successfully.");
        } else {
            throw new RuntimeException("Address not found with ID: " + id);
        }
    }

    // Lấy tài khoản qua email và danh sách địa chỉ
    public AccountDTO getAccountByEmail() {
        // Lấy email người dùng từ SecurityContext
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> accountOptional = accountRepository.findByEmail(email);

        if (!accountOptional.isPresent()) {
            throw new RuntimeException("Account not found");
        }

        Account account = accountOptional.get();

        return new AccountDTO(
                account.getFullname(),
                account.getEmail(),
                account.getPhone(),
                account.getGender(),
                account.getPassword(),
                account.getRole().name(),
                account.getStatus(),
                account.getToken(),
                getAddressesByUserEmail() // Gọi phương thức getAddressesByUserEmail để lấy địa chỉ
        );
    }




//    @Transactional
//    public Address setDefaultAddress(Integer addressId, String email) {
//        // Tìm tài khoản từ email
//        Account account = accountRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email đã đăng nhập."));
//        Integer accountId = account.getAccountId();
//
//        // Kiểm tra nếu địa chỉ tồn tại và thuộc về tài khoản
//        Optional<Address> addressOpt = addressRepository.findById(addressId);
//        if (addressOpt.isEmpty() || !addressOpt.get().getAccount().getAccountId().equals(accountId)) {
//            throw new RuntimeException("Địa chỉ không tồn tại hoặc không thuộc về tài khoản.");
//        }
//
//        // Đặt tất cả các địa chỉ hiện tại thành không mặc định
//        addressRepository.removeDefaultAddress(accountId);
//
//        // Lấy địa chỉ mặc định mới và đặt giá trị isDefault = true
//        Address defaultAddress = addressOpt.get();
//        defaultAddress.setIsDefault(true);
//
//        // Cập nhật địa chỉ trong cơ sở dữ liệu
//        addressRepository.save(defaultAddress);
//
//        // Cập nhật lại danh sách địa chỉ của tài khoản (đảm bảo địa chỉ mặc định xuất hiện trong danh sách)
//        List<Address> addresses = account.getAddresses();
//        addresses.forEach(address -> address.setIsDefault(false)); // Đảm bảo tất cả địa chỉ khác không phải là mặc định
//        addresses.add(defaultAddress); // Thêm địa chỉ mặc định vào danh sách nếu chưa có
//        account.setAddresses(addresses);
//
//        // Lưu lại tài khoản với địa chỉ đã cập nhật
//        accountRepository.save(account);
//
//        // Trả về địa chỉ mặc định
//        return defaultAddress;
//    }



    @Transactional
    public Address setDefaultAddress(Integer addressId, String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email đã đăng nhập."));

        Optional<Address> addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isEmpty() || !addressOpt.get().getAccount().getAccountId().equals(account.getAccountId())) {
            throw new RuntimeException("Địa chỉ không tồn tại hoặc không thuộc về tài khoản.");
        }

        addressRepository.removeDefaultAddress(account.getAccountId());

        Address defaultAddress = addressOpt.get();
        defaultAddress.setIsDefault(true);
        addressRepository.save(defaultAddress);  // Lưu lại địa chỉ mặc định

        return defaultAddress;  // Trả về địa chỉ đã được cập nhật
    }








}

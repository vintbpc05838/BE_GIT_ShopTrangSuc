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
        if (address.getIsDefault() == null) {
            address.setIsDefault(false); // Gán mặc định là false nếu isDefault là null
        }
        return addressRepository.save(address);
    }

    // Lấy danh sách địa chỉ của người dùng qua email
    public List<AddressDTO> getAddressesByUserEmail() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> accountOptional = accountRepository.findByEmail(email);

        if (!accountOptional.isPresent()) {
            throw new RuntimeException("Account not found");
        }

        Account account = accountOptional.get();
        List<Address> addresses = addressRepository.findByAccount(account);

        return addresses.stream()
                .map(address -> new AddressDTO(
                        address.getAddressId(),
                        address.getAddress(),
                        address.getCity(),
                        address.getDistrict(),
                        address.getWard(), // Thêm ward vào AddressDTO
                        address.getIsDefault() != null ? address.getIsDefault() : false))
                .collect(Collectors.toList());
    }

    // Cập nhật địa chỉ
    public AddressDTO updateAddress(Integer id, AddressDTO updatedAddressDTO) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setAddress(updatedAddressDTO.getAddress());
        address.setCity(updatedAddressDTO.getCity());
        address.setDistrict(updatedAddressDTO.getDistrict());
        address.setWard(updatedAddressDTO.getWard()); // Cập nhật ward
        address.setIsDefault(updatedAddressDTO.getIsDefault() != null ? updatedAddressDTO.getIsDefault() : false);

        Address updatedAddress = addressRepository.save(address);
        return new AddressDTO(updatedAddress.getAddressId(), updatedAddress.getAddress(),
                updatedAddress.getCity(), updatedAddress.getDistrict(),
                updatedAddress.getWard(), updatedAddress.getIsDefault());
    }

    // Xóa địa chỉ
    public void deleteAddressById(Integer id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
        } else {
            throw new RuntimeException("Address not found with ID: " + id);
        }
    }

    // Lấy tài khoản qua email và danh sách địa chỉ
    public AccountDTO getAccountByEmail() {
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

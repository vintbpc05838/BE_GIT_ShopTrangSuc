package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.DTO.AccountDTO;
import com.poly.shoptrangsuc.Exceptions.EmailAlreadyExistsException;
import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Model.Address;
import com.poly.shoptrangsuc.Model.Role;
import com.poly.shoptrangsuc.Repository.AccountRepository;
import com.poly.shoptrangsuc.Repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminAccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepository addressRepository;



    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> getAllManagers() {
        return accountRepository.findByRole(Role.ADMIN);
    }


    public List<Account> getAllStaff() {
        return accountRepository.findByRole(Role.STAFF);
    }

    public List<Account> getAllCustomers() {
        return accountRepository.findByRole(Role.USER);
    }




    public Account getAccountById(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + id));
    }



    public Account createManagementAccount(AccountDTO accountDTO) {
        // Kiểm tra email đã tồn tại
        Optional<Account> existingAccount = accountRepository.findByEmail(accountDTO.getEmail());
        if (existingAccount.isPresent()) {
            throw new EmailAlreadyExistsException("Email này đã được đăng ký. Vui lòng sử dụng email khác.");
        }

        // Tạo đối tượng Account từ DTO
        Account account = new Account();
        account.setFullname(accountDTO.getFullname());
        account.setEmail(accountDTO.getEmail());
        account.setGender(accountDTO.getGender());
        account.setPhone(accountDTO.getPhone());
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        account.setRole(Role.ADMIN);
        account.setStatus("Hoạt động");

        // Xử lý địa chỉ
        if (accountDTO.getAddresses() != null) {
            List<Address> addresses = accountDTO.getAddresses().stream()
                    .map(addressDTO -> {
                        Address address = new Address();
                        address.setAddress(addressDTO.getAddress());
                        address.setCity(addressDTO.getCity());
                        address.setDistrict(addressDTO.getDistrict());
                        address.setIsDefault(addressDTO.getIsDefault());
                        address.setAccount(account); // Liên kết địa chỉ với tài khoản
                        return address;
                    })
                    .collect(Collectors.toList());
            account.setAddresses(addresses); // Gán danh sách địa chỉ cho tài khoản
        }

        // Lưu tài khoản và địa chỉ
        Account savedAccount = accountRepository.save(account);

        // Lưu các địa chỉ (nếu có)
        if (account.getAddresses() != null && !account.getAddresses().isEmpty()) {
            addressRepository.saveAll(account.getAddresses());
        }

        return savedAccount;
    }


    public Account createStaffAccount(AccountDTO accountDTO) {

        Optional<Account> existingAccount = accountRepository.findByEmail(accountDTO.getEmail());
        if (existingAccount.isPresent()) {
            throw new EmailAlreadyExistsException("Email này đã được đăng ký. Vui lòng sử dụng email khác.");
        }

        Account account = new Account();
        account.setFullname(accountDTO.getFullname());
        account.setEmail(accountDTO.getEmail());
        account.setGender(accountDTO.getGender());
        account.setPhone(accountDTO.getPhone());
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        account.setRole(Role.STAFF);
        account.setStatus("Hoạt động");
        // Xử lý địa chỉ
        if (accountDTO.getAddresses() != null) {
            List<Address> addresses = accountDTO.getAddresses().stream()
                    .map(addressDTO -> {
                        Address address = new Address();
                        address.setAddress(addressDTO.getAddress());
                        address.setCity(addressDTO.getCity());
                        address.setDistrict(addressDTO.getDistrict());
                        address.setIsDefault(addressDTO.getIsDefault());
                        address.setAccount(account); // Liên kết địa chỉ với tài khoản
                        return address;
                    })
                    .collect(Collectors.toList());
            account.setAddresses(addresses); // Gán danh sách địa chỉ cho tài khoản
        }

        // Lưu tài khoản và địa chỉ
        Account savedAccount = accountRepository.save(account);

        // Lưu các địa chỉ (nếu có)
        if (account.getAddresses() != null && !account.getAddresses().isEmpty()) {
            addressRepository.saveAll(account.getAddresses());
        }

        return savedAccount;
    }




    // Thêm phương thức xóa tài khoản bằng ID nếu cần thiết
    public void deleteAccountById(Integer id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
        } else {
            throw new RuntimeException("Account not found with ID: " + id);
        }
    }
}

package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.DTO.AccountDTO;
import com.poly.shoptrangsuc.DTO.AddressDTO;
import com.poly.shoptrangsuc.DTO.ChangePasswordDTO;
import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Model.Address;
import com.poly.shoptrangsuc.Repository.AccountRepository;
import com.poly.shoptrangsuc.Repository.AddressRepository;
import com.poly.shoptrangsuc.Service.Impl.JWTServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffInformatiomService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private JWTServiceImpl jwtTokenUtil;

    public AccountDTO getAccountDetails(String token) {
        String email;
        try {
            email = jwtTokenUtil.getUserEmailFromToken(token); // Lấy email từ token
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy thông tin từ token: " + e.getMessage());
        }

        // Tìm tài khoản theo email, nếu không có sẽ ném ngoại lệ
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        // Chuyển đổi thành AccountDTO
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setFullname(account.getFullname());
        accountDTO.setEmail(account.getEmail());
        accountDTO.setPhone(account.getPhone());

        // Chuyển đổi giới tính từ Boolean (true là Nam, false là Nữ)
        accountDTO.setGender(account.getGender());

        // Xử lý role (nếu role là enum, cần gọi .name())
        accountDTO.setRole(account.getRole() != null ? account.getRole().name() : "");

        // Xử lý status
        accountDTO.setStatus(account.getStatus());

        // Chuyển đổi addresses từ Account sang AccountDTO
        if (account.getAddresses() != null) {
            List<AddressDTO> addressDTOs = account.getAddresses().stream()
                    .map(address -> new AddressDTO(address.getAddressId(),address.getAddress(), address.getCity(), address.getDistrict(), address.getIsDefault()))
                    .collect(Collectors.toList());
            accountDTO.setAddresses(addressDTOs);
        } else {
            accountDTO.setAddresses(new ArrayList<>());
        }

        return accountDTO;
    }




    public AccountDTO saveAccountDetails(AccountDTO accountDTO) {
        // Kiểm tra tài khoản đã tồn tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();  // email của người dùng đăng nhập

        // Tìm tài khoản theo email của người dùng đang đăng nhập
        Account account = accountRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        // Cập nhật thông tin tài khoản
        account.setFullname(accountDTO.getFullname());
        account.setEmail(accountDTO.getEmail());
        account.setPhone(accountDTO.getPhone());
        account.setGender(accountDTO.getGender());


        // Cập nhật địa chỉ
        for (AddressDTO addressDTO : accountDTO.getAddresses()) {
            Address address;
            if (addressDTO.getAddressId() != null) {
                // Cập nhật địa chỉ đã có
                address = addressRepository.findById(addressDTO.getAddressId())
                        .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại"));
                address.setAddress(addressDTO.getAddress());
                address.setCity(addressDTO.getCity());
                address.setDistrict(addressDTO.getDistrict());
                address.setIsDefault(addressDTO.getIsDefault());
            } else {
                // Thêm địa chỉ mới nếu không có ID
                address = new Address();
                address.setAddress(addressDTO.getAddress());
                address.setCity(addressDTO.getCity());
                address.setDistrict(addressDTO.getDistrict());
                address.setIsDefault(addressDTO.getIsDefault());
                address.setAccount(account);  // Gán tài khoản cho địa chỉ
            }
            // Lưu địa chỉ vào DB
            addressRepository.save(address);
        }

        // Lưu lại tài khoản sau khi cập nhật
        Account updatedAccount = accountRepository.save(account);

        // Chuyển đổi Account thành AccountDTO để trả về
//        return new AccountDTO(updatedAccount);
        return accountDTO;
    }



    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInEmail = authentication.getName();  // email của người dùng đăng nhập

        // Tìm tài khoản theo email của người dùng đang đăng nhập
        Account account = accountRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        // Kiểm tra mật khẩu hiện tại có đúng không
        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), account.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng!");
        }

        // Kiểm tra mật khẩu mới và mật khẩu xác nhận có khớp không
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu mới và mật khẩu xác nhận không khớp!");
        }

        // Mã hóa mật khẩu mới
        String encodedNewPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());

        // Cập nhật mật khẩu mới
        account.setPassword(encodedNewPassword);

        // Lưu lại tài khoản với mật khẩu mới
        accountRepository.save(account);
    }



}

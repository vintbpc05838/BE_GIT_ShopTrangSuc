package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.DTO.AccountDTO;
import com.poly.shoptrangsuc.DTO.ContactMessage;
import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Repository.AccountRepository;
import com.poly.shoptrangsuc.Service.Impl.JWTServiceImpl;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    private AccountRepository accountRepository;

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


        return accountDTO;
    }



    @Autowired
    private JavaMailSender mailSender;

    private static final String EMAIL_TO = "gamttpc05905@fpt.edu.vn"; // Email nhận
    private static final String PHONE_TO = "0799624325"; // Số điện thoại nhận (ví dụ sẽ gửi SMS qua SMS service nếu cần)

    public void sendEmail(ContactMessage contactMessage) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Cấu hình email
        helper.setFrom(contactMessage.getEmail());
        helper.setTo(EMAIL_TO);
        helper.setSubject("Liên hệ từ " + contactMessage.getFullname());
        helper.setText(
                "Thông tin liên hệ:\n" +
                        "Họ tên: " + contactMessage.getFullname() + "\n" +
                        "Email: " + contactMessage.getEmail() + "\n" +
                        "Số điện thoại: " + contactMessage.getPhone() + "\n\n" +
                        "Nội dung:\n" + contactMessage.getMessage(), true
        );

        mailSender.send(message);
    }
}

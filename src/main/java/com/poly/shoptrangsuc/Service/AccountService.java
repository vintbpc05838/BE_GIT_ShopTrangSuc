package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.Model.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface AccountService {
    UserDetailsService userDetailsService();
    Optional<Account> findByEmail(String email);

}

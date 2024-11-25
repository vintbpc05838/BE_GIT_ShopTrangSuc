package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.DTO.AccountDTO;
import com.poly.shoptrangsuc.Model.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;


public interface AccountService {
    UserDetailsService userDetailsService();
    Optional<Account> findByEmail(String email);

}

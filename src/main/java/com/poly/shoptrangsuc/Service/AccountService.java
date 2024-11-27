package com.poly.shoptrangsuc.Service;

import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service

public interface AccountService {
    UserDetailsService userDetailsService();
    Optional<Account> findByEmail(String email);
    @Autowired
    AccountRepository accountRepository = null;



    public default Account getAccountById(Long accountId) {
        return accountRepository.findById(Math.toIntExact(accountId)).orElse(null);
    }

}

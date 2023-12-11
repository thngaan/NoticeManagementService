package com.ptn.noticemanagementservice.usermanagement.service.impl;

import com.ptn.noticemanagementservice.usermanagement.dto.AccountDto;
import com.ptn.noticemanagementservice.usermanagement.entity.Account;
import com.ptn.noticemanagementservice.usermanagement.repository.AccountRepository;
import com.ptn.noticemanagementservice.usermanagement.request.RegistrationRequest;
import com.ptn.noticemanagementservice.usermanagement.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email/Phone number is not found, " + username));
    }

    @Override
    public AccountDto register(RegistrationRequest registrationDto) {
            Account account = new Account();
            account.setAccountStatus("ACTIVE");
            account.setEmail(registrationDto.getEmail());
            account.setPhoneNumber(registrationDto.getPhoneNumber());
            account.setUsername(registrationDto.getUsername());
            account.setFullName(registrationDto.getFullName());
            account.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            accountRepository.save(account);
            return new AccountDto(account.getUsername());
    }

    @Override
    public boolean existsByUsernameOrEmail(String username, String email) {
        return accountRepository.existsByUsernameOrEmail(username, email);
    }
}

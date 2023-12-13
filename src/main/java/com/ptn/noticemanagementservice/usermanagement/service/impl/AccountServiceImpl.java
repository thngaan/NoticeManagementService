package com.ptn.noticemanagementservice.usermanagement.service.impl;

import com.ptn.noticemanagementservice.common.exception.NoticeException;
import com.ptn.noticemanagementservice.common.exception.ResourceNotFoundException;
import com.ptn.noticemanagementservice.usermanagement.dto.AccountDto;
import com.ptn.noticemanagementservice.usermanagement.entity.Account;
import com.ptn.noticemanagementservice.usermanagement.mapper.AccountMapper;
import com.ptn.noticemanagementservice.usermanagement.repository.AccountRepository;
import com.ptn.noticemanagementservice.usermanagement.request.RegistrationRequest;
import com.ptn.noticemanagementservice.usermanagement.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountMapper = accountMapper;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> {
                    LOGGER.error("Username {} is not found", username);
                    return new UsernameNotFoundException("Username is not found." + username);
                });
    }

    @Override
    public AccountDto register(RegistrationRequest registrationRequest) {
        // Check exist username or email
        if (existsByUsernameOrEmail(registrationRequest.getUsername(), registrationRequest.getEmail())) {
            LOGGER.error("Username Or Email is already taken");
            throw new NoticeException("Username Or Email is already taken");
        }
        Account account = accountMapper.toEntity(registrationRequest);
        account.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        Account newAccount = accountRepository.save(account);
        return accountMapper.toDto(newAccount);
    }

    @Override
    public boolean existsByUsernameOrEmail(String username, String email) {
        return accountRepository.existsByUsernameOrEmail(username, email);
    }

    @Override
    public Account getByUserName(String username) throws ResourceNotFoundException {
        return accountRepository.findByUsername(username).orElseThrow(() -> {
            LOGGER.error("Account with username = {} is not found", username);
            return new ResourceNotFoundException("Account", "username", username);
        });
    }
}

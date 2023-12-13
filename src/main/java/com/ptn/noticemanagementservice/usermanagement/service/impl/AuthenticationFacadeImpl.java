package com.ptn.noticemanagementservice.usermanagement.service.impl;

import com.ptn.noticemanagementservice.common.exception.ResourceNotFoundException;
import com.ptn.noticemanagementservice.usermanagement.entity.Account;
import com.ptn.noticemanagementservice.usermanagement.service.AccountService;
import com.ptn.noticemanagementservice.usermanagement.service.AuthenticationFacade;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final AccountService accountService;

    public AuthenticationFacadeImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public Account getAuthenticationAccount() throws ResourceNotFoundException {
        return accountService.getByUserName(getAuthentication().getName());
    }

    @Override
    public boolean isSameAccount(Account account) {
        return account.getUsername().equals(getAuthentication().getName());
    }
}
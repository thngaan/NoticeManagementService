package com.ptn.noticemanagementservice.usermanagement.service;

import com.ptn.noticemanagementservice.common.exception.ResourceNotFoundException;
import com.ptn.noticemanagementservice.usermanagement.dto.AccountDto;
import com.ptn.noticemanagementservice.usermanagement.entity.Account;
import com.ptn.noticemanagementservice.usermanagement.request.RegistrationRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {

    /**
     * Get by username
     *
     * @param username is account's username
     * @return Account
     * @throws ResourceNotFoundException if not found username
     */
    Account getByUserName(String username) throws ResourceNotFoundException;

    /**
     * Check exist by username or email
     *
     * @param username - is account's username
     * @param email    - is account's email
     * @return true/false
     */
    boolean existsByUsernameOrEmail(String username, String email);

    /**
     * Register new account
     *
     * @param registrationRequest is request from user
     * @return accountDto
     */
    AccountDto register(RegistrationRequest registrationRequest);

}

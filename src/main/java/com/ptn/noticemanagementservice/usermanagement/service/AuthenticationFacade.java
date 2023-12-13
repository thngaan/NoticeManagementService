package com.ptn.noticemanagementservice.usermanagement.service;

import com.ptn.noticemanagementservice.common.exception.ResourceNotFoundException;
import com.ptn.noticemanagementservice.usermanagement.entity.Account;
import org.springframework.security.core.Authentication;

/**
 * Authentication Facade
 * include method regarding existed authentication
 */
public interface AuthenticationFacade {

    /**
     * Get Current Authentication
     *
     * @return Authentication
     */
    Authentication getAuthentication();

    /**
     * Get current authentication account
     *
     * @return Account
     * @throws ResourceNotFoundException
     */
    Account getAuthenticationAccount() throws ResourceNotFoundException;

    /**
     * Check is input account same with login account
     *
     * @param account
     * @return true/false
     */
    boolean isSameAccount(Account account);
}
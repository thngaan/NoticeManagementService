package com.ptn.noticemanagementservice.usermanagement.service;

import com.ptn.noticemanagementservice.common.exception.ResourceNotFoundException;
import com.ptn.noticemanagementservice.usermanagement.dto.AccountDto;
import com.ptn.noticemanagementservice.usermanagement.entity.Account;
import com.ptn.noticemanagementservice.usermanagement.request.RegistrationRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {

    boolean existsByUsernameOrEmail(String username, String email);

    AccountDto register(RegistrationRequest registrationDto);

    Account getByUserName(String username) throws ResourceNotFoundException;
}

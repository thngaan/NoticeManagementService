package com.ptn.noticemanagementservice.usermanagement.controller;

import com.ptn.noticemanagementservice.common.builder.GenericResponseBuilder;
import com.ptn.noticemanagementservice.usermanagement.dto.AccountDto;
import com.ptn.noticemanagementservice.usermanagement.request.LoginRequest;
import com.ptn.noticemanagementservice.usermanagement.request.RegistrationRequest;
import com.ptn.noticemanagementservice.usermanagement.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ptn.noticemanagementservice.common.contant.RestURI.*;

/**
 * Authentication Controller
 */
@RestController
@RequestMapping(API)
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AccountService accountService) {
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
    }

    /**
     * Login API
     *
     * @param loginRequest login information from user
     * @return status
     */
    @PostMapping(path = LOGIN)
    public ResponseEntity<?> authenticateUser(@RequestBody @Validated LoginRequest loginRequest) {

        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        this.authenticationManager.authenticate(authenticationRequest);
        return ResponseEntity.ok("User login successfully!");
    }

    /**
     * Registration API
     *
     * @param registrationRequest registration request from user
     * @return AccountDto
     */
    @PostMapping(REGISTRATION)
    public ResponseEntity<?> registerUser(@Validated @RequestBody RegistrationRequest registrationRequest) {
        AccountDto accountDto = accountService.register(registrationRequest);
        return ResponseEntity.ok((new GenericResponseBuilder()).setData(accountDto).buildSuccess());
    }
}


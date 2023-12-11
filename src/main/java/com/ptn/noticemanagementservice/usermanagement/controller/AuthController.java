package com.ptn.noticemanagementservice.usermanagement.controller;

import com.ptn.noticemanagementservice.common.builder.GenericResponseBuilder;
import com.ptn.noticemanagementservice.usermanagement.dto.AccountDto;
import com.ptn.noticemanagementservice.usermanagement.request.LoginRequest;
import com.ptn.noticemanagementservice.usermanagement.request.RegistrationRequest;
import com.ptn.noticemanagementservice.usermanagement.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private AccountService accountService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AccountService accountService) {
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Validated LoginRequest loginRequest) {

        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);
        return ResponseEntity.ok("User login successfully!");
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@Validated @RequestBody RegistrationRequest registrationRequest) throws Exception {
        if (accountService.existsByUsernameOrEmail(registrationRequest.getUsername(), registrationRequest.getEmail())) {
            return ResponseEntity.badRequest().body((new GenericResponseBuilder()).setMessage("Error: Username is already taken!").buildFailure());
        } else {
            AccountDto accountDto = accountService.register(registrationRequest);
            return ResponseEntity.ok((new GenericResponseBuilder()).setData(accountDto).buildSuccess());
        }
    }
}


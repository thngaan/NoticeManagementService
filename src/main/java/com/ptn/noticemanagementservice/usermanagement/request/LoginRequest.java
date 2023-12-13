package com.ptn.noticemanagementservice.usermanagement.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class LoginRequest {

    private String username;

    private String password;
}

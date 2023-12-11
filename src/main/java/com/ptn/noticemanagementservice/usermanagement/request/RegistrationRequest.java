package com.ptn.noticemanagementservice.usermanagement.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class RegistrationRequest {

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String fullName;
}

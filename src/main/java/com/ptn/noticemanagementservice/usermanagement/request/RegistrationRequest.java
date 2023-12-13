package com.ptn.noticemanagementservice.usermanagement.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

@Data
@EqualsAndHashCode
public class RegistrationRequest {

    @NotBlank
    @Length(max = 255)
    private String username;

    @NotBlank
    @Length(max = 255)
    private String password;

    @NotBlank
    @Length(max = 255)
    @Email
    private String email;

    @NotBlank
    @Length(max = 255)
    private String phoneNumber;

    @NotBlank
    @Length(max = 255)
    private String fullName;
}

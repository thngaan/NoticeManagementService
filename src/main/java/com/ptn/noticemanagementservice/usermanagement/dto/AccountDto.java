package com.ptn.noticemanagementservice.usermanagement.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class AccountDto {

    private String username;

    public AccountDto(String username) {
        this.username = username;
    }
}

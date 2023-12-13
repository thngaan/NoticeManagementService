package com.ptn.noticemanagementservice.usermanagement.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AccountDto
 */
@Data
@EqualsAndHashCode
public class AccountDto {

    private Long id;

    private String username;

    public AccountDto() {
    }

    public AccountDto(String username) {
        this.username = username;
    }
}

package com.ptn.noticemanagementservice.usermanagement.mapper;

import com.ptn.noticemanagementservice.common.mapper.RequestResponseMapper;
import com.ptn.noticemanagementservice.noticemanagement.mapper.DocumentMapper;
import com.ptn.noticemanagementservice.usermanagement.dto.AccountDto;
import com.ptn.noticemanagementservice.usermanagement.entity.Account;
import com.ptn.noticemanagementservice.usermanagement.request.RegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class})
@Component
public interface AccountMapper extends RequestResponseMapper<RegistrationRequest, Account, AccountDto> {

    @Simple
    AccountDto toDto(Account notice);

    @Simple
    @Mapping(target = "password", ignore = true)
    Account toEntity(RegistrationRequest registrationRequest);
}
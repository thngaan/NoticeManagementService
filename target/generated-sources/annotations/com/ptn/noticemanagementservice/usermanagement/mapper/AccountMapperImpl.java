package com.ptn.noticemanagementservice.usermanagement.mapper;

import com.ptn.noticemanagementservice.usermanagement.dto.AccountDto;
import com.ptn.noticemanagementservice.usermanagement.entity.Account;
import com.ptn.noticemanagementservice.usermanagement.request.RegistrationRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-13T16:52:38+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public List<AccountDto> toDtoList(Collection<Account> entities) {
        if ( entities == null ) {
            return null;
        }

        List<AccountDto> list = new ArrayList<AccountDto>( entities.size() );
        for ( Account account : entities ) {
            list.add( toDto( account ) );
        }

        return list;
    }

    @Override
    public List<Account> toEntityList(Collection<RegistrationRequest> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Account> list = new ArrayList<Account>( entities.size() );
        for ( RegistrationRequest registrationRequest : entities ) {
            list.add( toEntity( registrationRequest ) );
        }

        return list;
    }

    @Override
    public void updateEntity(RegistrationRequest request, Account entity) {
        if ( request == null ) {
            return;
        }

        entity.setUsername( request.getUsername() );
        entity.setFullName( request.getFullName() );
        entity.setEmail( request.getEmail() );
        entity.setPhoneNumber( request.getPhoneNumber() );
        entity.setPassword( request.getPassword() );
    }

    @Override
    public AccountDto toDto(Account notice) {
        if ( notice == null ) {
            return null;
        }

        String username = null;

        username = notice.getUsername();

        AccountDto accountDto = new AccountDto( username );

        return accountDto;
    }

    @Override
    public Account toEntity(RegistrationRequest registrationRequest) {
        if ( registrationRequest == null ) {
            return null;
        }

        Account account = new Account();

        account.setUsername( registrationRequest.getUsername() );
        account.setFullName( registrationRequest.getFullName() );
        account.setEmail( registrationRequest.getEmail() );
        account.setPhoneNumber( registrationRequest.getPhoneNumber() );

        return account;
    }
}

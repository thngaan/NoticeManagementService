package com.ptn.noticemanagementservice.usermanagement.repository;

import com.ptn.noticemanagementservice.usermanagement.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * AccountRepository
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Boolean existsByUsernameOrEmail(String username, String email);

}

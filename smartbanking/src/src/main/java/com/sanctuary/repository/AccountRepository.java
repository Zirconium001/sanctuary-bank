package com.sanctuary.repository;

import com.sanctuary.domain.account.Account;
import com.sanctuary.domain.account.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    List<Account> findByCustomerId(String customerId);
    List<Account> findByAccountType(AccountType accountType);
    long countByAccountType(AccountType accountType);
}

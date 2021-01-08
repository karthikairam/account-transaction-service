package com.demo.mobilebank.account.repository;

import com.demo.mobilebank.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(final long accountNumber);
}

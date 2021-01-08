package com.demo.mobilebank.account.repository;

import com.demo.mobilebank.account.entity.MoneyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyTransactionRepository extends JpaRepository<MoneyTransaction, Long> {
}

package com.demo.mobilebank.account.service;

import com.demo.mobilebank.account.bean.request.AccountRequest;
import com.demo.mobilebank.account.entity.Account;

import java.math.BigDecimal;

public interface AccountService {
    Account retrieve(final String accountNumber);
    Account createAccount(AccountRequest account);
    void makeFundTransfer(final long fromAccountNumber, final long toAccountNumber, final BigDecimal fund);
}

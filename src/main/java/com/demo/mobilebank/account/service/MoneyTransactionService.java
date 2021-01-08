package com.demo.mobilebank.account.service;

import com.demo.mobilebank.account.bean.request.MoneyTransactionRequest;
import com.demo.mobilebank.account.entity.MoneyTransaction;

public interface MoneyTransactionService {
    MoneyTransaction transferMoney(final MoneyTransactionRequest moneyTxReq);
}

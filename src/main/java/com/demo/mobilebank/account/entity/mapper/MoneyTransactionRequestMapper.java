package com.demo.mobilebank.account.entity.mapper;

import com.demo.mobilebank.account.bean.request.MoneyTransactionRequest;
import com.demo.mobilebank.account.entity.MoneyTransaction;
import org.springframework.stereotype.Component;

@Component
public class MoneyTransactionRequestMapper implements RequestMapper<MoneyTransactionRequest, MoneyTransaction> {

    @Override
    public MoneyTransaction mapFrom(MoneyTransactionRequest request) {
        return MoneyTransaction.builder()
                .fromAccountNumber(request.getFromAccountNumber())
                .toAccountNumber(request.getToAccountNumber())
                .transactionAmount(request.getTransactionAmount())
                .referenceNotes(request.getReferenceNotes())
                .build();
    }
}

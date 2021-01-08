package com.demo.mobilebank.account.entity.mapper;

import com.demo.mobilebank.account.bean.request.AccountRequest;
import com.demo.mobilebank.account.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountRequestMapper implements RequestMapper<AccountRequest, Account> {

    @Override
    public Account mapFrom(AccountRequest request) {
        return Account.builder()
                .holderFirstName(request.getHolderFirstName())
                .holderLastName(request.getHolderLastName())
                .balance(request.getBalance())
                .build();
    }
}

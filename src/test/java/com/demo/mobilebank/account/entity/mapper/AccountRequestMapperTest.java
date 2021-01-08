package com.demo.mobilebank.account.entity.mapper;

import com.demo.mobilebank.account.bean.request.AccountRequest;
import com.demo.mobilebank.account.entity.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountRequestMapperTest {

    @InjectMocks
    private AccountRequestMapper accountRequestMapper;

    @Test
    void test_mapFrom_positive_case() {
        //Assemble
        final AccountRequest accountRequest = AccountRequest.builder()
                .holderFirstName("Firstname")
                .holderLastName("Lastname")
                .balance(new BigDecimal("1000"))
                .build();

        //Action
        final Account account = accountRequestMapper.mapFrom(accountRequest);

        //Assert
        assertNotNull(account);
        assertEquals(accountRequest.getBalance(), account.getBalance());
        assertEquals(accountRequest.getHolderFirstName(), account.getHolderFirstName());
        assertEquals(accountRequest.getHolderLastName(), account.getHolderLastName());
        assertFalse(account.getAccountNumber() > 0);
    }
}
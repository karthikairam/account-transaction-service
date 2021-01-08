package com.demo.mobilebank.account.entity.mapper;

import com.demo.mobilebank.account.bean.request.MoneyTransactionRequest;
import com.demo.mobilebank.account.entity.MoneyTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MoneyTransactionRequestMapperTest {

    @InjectMocks
    private MoneyTransactionRequestMapper moneyTransactionRequestMapper;

    @Test
    void test_mapFrom_positive_case() {
        //Assemble
        final MoneyTransactionRequest request = MoneyTransactionRequest.builder()
                .fromAccountNumber(1000010)
                .toAccountNumber(1000020)
                .referenceNotes("Fund transfer to friend 2.")
                .transactionAmount(new BigDecimal("1000"))
                .build();
        //Action
        final MoneyTransaction transaction = moneyTransactionRequestMapper.mapFrom(request);

        //Assert
        assertNotNull(transaction);
        assertEquals(request.getFromAccountNumber(), transaction.getFromAccountNumber());
        assertEquals(request.getToAccountNumber(), transaction.getToAccountNumber());
        assertEquals(request.getReferenceNotes(), transaction.getReferenceNotes());
        assertEquals(request.getTransactionAmount(), transaction.getTransactionAmount());
        assertFalse(transaction.getTransactionId() > 0);
    }
}
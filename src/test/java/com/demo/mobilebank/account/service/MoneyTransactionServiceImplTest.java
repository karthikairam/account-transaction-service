package com.demo.mobilebank.account.service;

import com.demo.mobilebank.account.bean.request.MoneyTransactionRequest;
import com.demo.mobilebank.account.entity.MoneyTransaction;
import com.demo.mobilebank.account.entity.mapper.RequestMapper;
import com.demo.mobilebank.account.exception.server.InternalServerException;
import com.demo.mobilebank.account.exception.server.NoRollBackException;
import com.demo.mobilebank.account.repository.MoneyTransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoneyTransactionServiceImplTest {

    @Mock
    private MoneyTransactionRepository moneyTransactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private RequestMapper<MoneyTransactionRequest, MoneyTransaction> moneyTransactionRequestMapper;

    @InjectMocks
    private MoneyTransactionServiceImpl moneyTransactionService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void test_transferMoney_positive_case() {
        //Assemble
        final MoneyTransaction moneyTransaction = MoneyTransaction.builder()
                .fromAccountNumber(1000010)
                .toAccountNumber(1000020)
                .transactionAmount(new BigDecimal("200"))
                .referenceNotes("Transferring to my friend2")
                .build();
        when(moneyTransactionRequestMapper.mapFrom(any(MoneyTransactionRequest.class)))
                .thenReturn(moneyTransaction);
        doNothing().when(accountService).makeFundTransfer(anyLong(), anyLong(), any(BigDecimal.class));
        verify(moneyTransactionRepository, atMostOnce()).save(moneyTransaction);
        when(moneyTransactionRepository.save(moneyTransaction))
                .thenAnswer(invocationOnMock -> {
                    final MoneyTransaction receivedTransaction = invocationOnMock.getArgument(0);
                    receivedTransaction.setTransactionId(1000001);
                    return receivedTransaction;
                });

        //Action
        final MoneyTransaction transaction = moneyTransactionService
                .transferMoney(mock(MoneyTransactionRequest.class));

        //Assert
        assertNotNull(transaction);
        assertEquals(1000001, transaction.getTransactionId());
        assertEquals("SUCCESS", transaction.getStatus());
        assertNotNull(transaction.getReferenceNotes());
    }

    @Test
    void test_transferMoney_negative_case1() {
        //AccountService's makeFundTransfer function throws exception

        //Assemble
        final MoneyTransaction moneyTransaction = MoneyTransaction.builder()
                .fromAccountNumber(1000010)
                .toAccountNumber(1000020)
                .transactionAmount(new BigDecimal("200"))
                .referenceNotes("Transferring to my friend2")
                .build();
        when(moneyTransactionRequestMapper.mapFrom(any(MoneyTransactionRequest.class)))
                .thenReturn(moneyTransaction);
        doThrow(new InternalServerException("Insufficient fund.")).when(accountService)
                .makeFundTransfer(anyLong(), anyLong(), any(BigDecimal.class));
        verify(moneyTransactionRepository, atMostOnce()).save(moneyTransaction);
        when(moneyTransactionRepository.save(moneyTransaction))
                .thenAnswer(invocationOnMock -> {
                    final MoneyTransaction receivedTransaction = invocationOnMock.getArgument(0);
                    assertNotNull(receivedTransaction);
                    assertEquals("FAILED", receivedTransaction.getStatus());
                    assertEquals("Insufficient fund.", receivedTransaction.getErrorDescription());
                    return receivedTransaction;
                });

        //Action && Assert
        assertThrows(NoRollBackException.class, () -> moneyTransactionService
                .transferMoney(mock(MoneyTransactionRequest.class)));

    }

    @Test
    void test_transferMoney_negative_case2() {
        //moneyTransactionRequestMapper fails to produce entity

        //Assemble
        when(moneyTransactionRequestMapper.mapFrom(any(MoneyTransactionRequest.class)))
                .thenReturn(null);
        verify(accountService, never()).makeFundTransfer(anyLong(), anyLong(), any(BigDecimal.class));
        verify(moneyTransactionRepository, never()).save(any(MoneyTransaction.class));

        //Action && Assert
        assertThrows(InternalServerException.class, () -> moneyTransactionService
                .transferMoney(mock(MoneyTransactionRequest.class)));

    }
}
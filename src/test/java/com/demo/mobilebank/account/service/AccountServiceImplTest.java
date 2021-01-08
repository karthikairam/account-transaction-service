package com.demo.mobilebank.account.service;

import com.demo.mobilebank.account.bean.request.AccountRequest;
import com.demo.mobilebank.account.entity.Account;
import com.demo.mobilebank.account.entity.mapper.RequestMapper;
import com.demo.mobilebank.account.exception.client.AccountNotFoundException;
import com.demo.mobilebank.account.exception.client.BadRequestException;
import com.demo.mobilebank.account.exception.server.InternalServerException;
import com.demo.mobilebank.account.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RequestMapper<AccountRequest, Account> accountRequestMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void test_retrieve_positive_case1() {
        //Assemble
        final long accountNumber = 1000001;
        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(
                        Account.builder()
                                .accountNumber(accountNumber)
                                .balance(new BigDecimal("1000.00"))
                                .holderLastName("Lastname")
                                .holderFirstName("Firstname")
                                .build()
                );

        //Action
        final Account account = accountService.retrieve(Long.toString(accountNumber));

        //Assert
        assertNotNull(account);
        assertEquals(accountNumber, account.getAccountNumber());
    }

    @Test
    void test_retrieve_negative_case1() {
        //Assemble && Action && Assert
        assertThrows(BadRequestException.class, () -> accountService.retrieve(null));
    }

    @Test
    void test_retrieve_negative_case2() {
        // Assemble
        when(accountRepository.findByAccountNumber(anyLong())).thenReturn(null);

        // Action && Assert
        assertThrows(AccountNotFoundException.class, () -> accountService.retrieve("12343"));
    }

    @Test
    void test_createAccount_positive_case() {
        //Assemble
        final Account dummyAccount = Account.builder()
                .balance(new BigDecimal("1000.00"))
                .holderLastName("Lastname")
                .holderFirstName("Firstname")
                .build();
        when(accountRequestMapper.mapFrom(any(AccountRequest.class)))
                .thenReturn(dummyAccount);
        when(accountRepository.save(any(Account.class)))
                .thenReturn(dummyAccount.toBuilder().accountNumber(10000012).build());

        //Action
        final Account account = accountService.createAccount(mock(AccountRequest.class));

        //Assert
        assertNotNull(account);
        assertEquals(10000012, account.getAccountNumber());
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
        assertEquals("Firstname", account.getHolderFirstName());
        assertEquals("Lastname", account.getHolderLastName());
    }

    @Test
    void test_createAccount_negative_case() {
        //Assemble
        when(accountRequestMapper.mapFrom(any(AccountRequest.class)))
                .thenReturn(null);

        //Action && Assert
        assertThrows(InternalServerException.class, () -> accountService.createAccount(mock(AccountRequest.class)));
    }

    @Test
    void test_makeFundTransfer_positive_case() {
        //Assemble
        final long fromAccountNumber = 10001;
        final long toAccountNumber = 10002;
        final BigDecimal fund = new BigDecimal("100");
        final Account fromAccount = Account.builder()
                .accountNumber(fromAccountNumber)
                .holderFirstName("Firstname1")
                .holderLastName("Lastname1")
                .balance(new BigDecimal("1000"))
                .build();

        final Account toAccount = Account.builder()
                .accountNumber(toAccountNumber)
                .holderFirstName("Firstname2")
                .holderLastName("Lastname2")
                .balance(new BigDecimal("1000"))
                .build();

        when(accountRepository.existsById(fromAccountNumber)).thenReturn(Boolean.TRUE);
        when(accountRepository.existsById(toAccountNumber)).thenReturn(Boolean.TRUE);
        when(accountRepository.findByAccountNumber(fromAccountNumber)).thenReturn(fromAccount);
        when(accountRepository.findByAccountNumber(toAccountNumber)).thenReturn(toAccount);
        when(accountRepository.saveAll(argThat(accounts -> {
            //Assert
            assertNotNull(accounts);
            final Iterator<Account> iterable = accounts.iterator();
            assertTrue(iterable.hasNext());
            assertEquals(new BigDecimal("900"), iterable.next().getBalance());
            assertTrue(iterable.hasNext());
            assertEquals(new BigDecimal("1100"), iterable.next().getBalance());
            return true;
        }))).thenReturn(List.of(fromAccount, toAccount));

        //Action
        accountService.makeFundTransfer(fromAccountNumber, toAccountNumber, fund);
    }

    @Test
    void test_makeFundTransfer_negative_case1() {
        //Insufficient funds in fromAccount

        //Assemble
        final long fromAccountNumber = 10001;
        final long toAccountNumber = 10002;
        final BigDecimal fund = new BigDecimal("100");
        final Account fromAccount = Account.builder()
                .accountNumber(fromAccountNumber)
                .holderFirstName("Firstname1")
                .holderLastName("Lastname1")
                .balance(new BigDecimal("50"))
                .build();

        when(accountRepository.existsById(fromAccountNumber)).thenReturn(Boolean.TRUE);
        when(accountRepository.existsById(toAccountNumber)).thenReturn(Boolean.TRUE);
        when(accountRepository.findByAccountNumber(fromAccountNumber)).thenReturn(fromAccount);
        verify(accountRepository, never()).findByAccountNumber(toAccountNumber);
        verify(accountRepository, never()).saveAll(anyIterable());

        //Action && Assert
        assertThrows(InternalServerException.class,
                () -> accountService.makeFundTransfer(fromAccountNumber, toAccountNumber, fund));
    }

    @Test
    void test_makeFundTransfer_negative_case2() {
        //Invalid fromAccount (meaning: not available in the system)

        //Assemble
        final long fromAccountNumber = 10001;
        final long toAccountNumber = 10002;
        final BigDecimal fund = new BigDecimal("100");

        when(accountRepository.existsById(fromAccountNumber)).thenReturn(Boolean.FALSE);
        verify(accountRepository, never()).existsById(toAccountNumber);
        verify(accountRepository, never()).findByAccountNumber(fromAccountNumber);
        verify(accountRepository, never()).findByAccountNumber(toAccountNumber);
        verify(accountRepository, never()).saveAll(anyIterable());

        //Action && Assert
        assertThrows(BadRequestException.class,
                () -> accountService.makeFundTransfer(fromAccountNumber, toAccountNumber, fund));
    }

    @Test
    void test_makeFundTransfer_negative_case3() {
        //Invalid toAccount (meaning: not available in the system)

        //Assemble
        final long fromAccountNumber = 10001;
        final long toAccountNumber = 10002;
        final BigDecimal fund = new BigDecimal("100");

        when(accountRepository.existsById(fromAccountNumber)).thenReturn(Boolean.TRUE);
        verify(accountRepository, atMostOnce()).existsById(toAccountNumber);
        when(accountRepository.existsById(toAccountNumber)).thenReturn(Boolean.FALSE);
        verify(accountRepository, never()).findByAccountNumber(fromAccountNumber);
        verify(accountRepository, never()).findByAccountNumber(toAccountNumber);
        verify(accountRepository, never()).saveAll(anyIterable());

        //Action && Assert
        assertThrows(BadRequestException.class,
                () -> accountService.makeFundTransfer(fromAccountNumber, toAccountNumber, fund));
    }

    @Test
    void test_makeFundTransfer_negative_case4() {
        //Invalid Money Transfer value

        //Assemble
        final long fromAccountNumber = 10001;
        final long toAccountNumber = 10002;
        final BigDecimal fund = new BigDecimal("-100");

        verify(accountRepository, never()).existsById(fromAccountNumber);
        verify(accountRepository, never()).existsById(toAccountNumber);
        verify(accountRepository, never()).findByAccountNumber(fromAccountNumber);
        verify(accountRepository, never()).findByAccountNumber(toAccountNumber);
        verify(accountRepository, never()).saveAll(anyIterable());

        //Action && Assert
        assertThrows(BadRequestException.class,
                () -> accountService.makeFundTransfer(fromAccountNumber, toAccountNumber, fund));
    }
}
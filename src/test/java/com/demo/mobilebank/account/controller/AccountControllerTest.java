package com.demo.mobilebank.account.controller;

import com.demo.mobilebank.account.bean.request.AccountRequest;
import com.demo.mobilebank.account.entity.Account;
import com.demo.mobilebank.account.exception.client.AccountNotFoundException;
import com.demo.mobilebank.account.exception.client.BadRequestException;
import com.demo.mobilebank.account.exception.server.InternalServerException;
import com.demo.mobilebank.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.demo.mobilebank.account.helper.TestHelper.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    public static final String HOLDER_FIRST_NAME = "Firstname";
    public static final String HOLDER_LAST_NAME = "Lastname";
    public static final String MONEY_BALANCE = "1000";
    public static final long CUSTOMER_ACCOUNT = 10000010;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @SneakyThrows
    @Test
    void test_getAccountDetail_positive_case() {
        //Assemble
        final Account account = Account.builder()
                .accountNumber(CUSTOMER_ACCOUNT)
                .holderFirstName(HOLDER_FIRST_NAME)
                .holderLastName(HOLDER_LAST_NAME)
                .balance(new BigDecimal(MONEY_BALANCE))
                .build();
        when(accountService.retrieve(anyString())).thenReturn(account);

        //Action & Assert
        mockMvc.perform(get("/v1/accounts/{accountNumber}", CUSTOMER_ACCOUNT).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.holderFirstName").value(HOLDER_FIRST_NAME))
                .andExpect(jsonPath("$.holderLastName").value(HOLDER_LAST_NAME))
                .andExpect(jsonPath("$.balance").value(MONEY_BALANCE))
                .andExpect(jsonPath("$.accountNumber").value(CUSTOMER_ACCOUNT));
    }

    @SneakyThrows
    @Test
    void test_getAccountDetail_negative_case1() {
        //Assemble
        when(accountService.retrieve(anyString())).thenThrow(new InternalServerException("Something went badly wrong."));

        //Action & Assert
        mockMvc.perform(get("/v1/accounts/{accountNumber}", CUSTOMER_ACCOUNT).accept(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("SERVER_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("Something went badly wrong."));
    }

    @SneakyThrows
    @Test
    void test_getAccountDetail_negative_case2() {
        //Assemble
        when(accountService.retrieve(anyString())).thenThrow(new BadRequestException("Something client has sent bad."));

        //Action & Assert
        mockMvc.perform(get("/v1/accounts/{accountNumber}", CUSTOMER_ACCOUNT).accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("CLIENT_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("Something client has sent bad."));
    }

    @SneakyThrows
    @Test
    void test_getAccountDetail_negative_case3() {
        //Assemble
        when(accountService.retrieve(anyString())).thenThrow(new AccountNotFoundException("Account not found."));

        //Action & Assert
        mockMvc.perform(get("/v1/accounts/{accountNumber}", CUSTOMER_ACCOUNT).accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("CLIENT_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("Account not found."));
    }

    @SneakyThrows
    @Test
    void test_getAccountDetail_negative_case4() {
        //Action & Assert
        mockMvc.perform(get("/v1/accounts/{accountNumber}", 100001).accept(APPLICATION_XML))
                .andExpect(status().is4xxClientError());
    }

    @SneakyThrows
    @Test
    void test_createAccount_positive_case() {
        //Assemble
        final Account account = Account.builder()
                .accountNumber(CUSTOMER_ACCOUNT)
                .holderFirstName(HOLDER_FIRST_NAME)
                .holderLastName(HOLDER_LAST_NAME)
                .balance(new BigDecimal(MONEY_BALANCE))
                .build();
        when(accountService.createAccount(any(AccountRequest.class))).thenReturn(account);

        //Action & Assert
        mockMvc.perform(post("/v1/accounts")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(asJsonString(AccountRequest.builder()
                        .holderFirstName(HOLDER_FIRST_NAME)
                        .holderLastName(HOLDER_LAST_NAME)
                        .balance(new BigDecimal(MONEY_BALANCE))
                        .build())
                ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.holderFirstName").value(HOLDER_FIRST_NAME))
                .andExpect(jsonPath("$.holderLastName").value(HOLDER_LAST_NAME))
                .andExpect(jsonPath("$.balance").value(MONEY_BALANCE))
                .andExpect(jsonPath("$.accountNumber").value(CUSTOMER_ACCOUNT));
    }
}
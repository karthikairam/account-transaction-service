package com.demo.mobilebank.account.controller;

import com.demo.mobilebank.account.bean.request.MoneyTransactionRequest;
import com.demo.mobilebank.account.entity.MoneyTransaction;
import com.demo.mobilebank.account.service.MoneyTransactionService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static com.demo.mobilebank.account.helper.TestHelper.asJsonString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MoneyTransactionController.class)
class MoneyTransactionControllerTest {

    public static final int FROM_ACCOUNT_NUMBER = 1000010;
    public static final int TO_ACCOUNT_NUMBER = 1000012;
    public static final String SUCCESS = "SUCCESS";
    public static final String REFERENCE_NOTES = "Fund Transfer to friend 2";
    public static final int TRANSACTION_ID = 1000010010;
    public static final String TRANSFER_MONEY = "1000";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MoneyTransactionService moneyTransactionService;

    @SneakyThrows
    @Test
    void test_transferMoney_positive() {
        //Assemble
        final MoneyTransaction transaction = MoneyTransaction.builder()
                .fromAccountNumber(FROM_ACCOUNT_NUMBER)
                .toAccountNumber(TO_ACCOUNT_NUMBER)
                .transactionAmount(new BigDecimal("1000"))
                .transactionId(TRANSACTION_ID)
                .referenceNotes(REFERENCE_NOTES)
                .status(SUCCESS)
                .build();
        when(moneyTransactionService.transferMoney(any(MoneyTransactionRequest.class))).thenReturn(transaction);

        //Action & Assert
        mockMvc.perform(post("/v1/transactions")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(asJsonString(MoneyTransactionRequest.builder()
                        .fromAccountNumber(FROM_ACCOUNT_NUMBER)
                        .toAccountNumber(TO_ACCOUNT_NUMBER)
                        .transactionAmount(new BigDecimal(TRANSFER_MONEY))
                        .referenceNotes(REFERENCE_NOTES)
                        .build())
                ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fromAccountNumber").value(FROM_ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.toAccountNumber").value(TO_ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.transactionAmount").value(TRANSFER_MONEY))
                .andExpect(jsonPath("$.transactionId").value(TRANSACTION_ID))
                .andExpect(jsonPath("$.referenceNotes").value(REFERENCE_NOTES))
                .andExpect(jsonPath("$.status").value(SUCCESS));
    }
}
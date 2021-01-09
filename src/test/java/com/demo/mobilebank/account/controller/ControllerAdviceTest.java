package com.demo.mobilebank.account.controller;

import com.demo.mobilebank.account.bean.request.AccountRequest;
import com.demo.mobilebank.account.bean.request.MoneyTransactionRequest;
import com.demo.mobilebank.account.exception.client.AccountNotFoundException;
import com.demo.mobilebank.account.exception.client.BadRequestException;
import com.demo.mobilebank.account.exception.server.InternalServerException;
import com.demo.mobilebank.account.service.AccountService;
import com.demo.mobilebank.account.service.MoneyTransactionService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.demo.mobilebank.account.controller.AccountControllerTest.CUSTOMER_ACCOUNT;
import static com.demo.mobilebank.account.helper.TestHelper.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AccountController.class, MoneyTransactionController.class})
@ActiveProfiles("test")
class ControllerAdviceTest {

    public static final String SOMETHING_WENT_BADLY_WRONG = "Something went badly wrong.";
    public static final String SERVER_ERROR = "SERVER_ERROR";
    public static final String CLIENT_ERROR = "CLIENT_ERROR";
    public static final String SOMETHING_CLIENT_HAS_SENT_BAD = "Something client has sent bad.";
    public static final String ACCOUNT_NOT_FOUND = "Account not found.";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private MoneyTransactionService moneyTransactionService;

    @SneakyThrows
    @Test
    void test_getAccountDetail_negative_case1() {
        //Assemble
        when(accountService.retrieve(anyLong())).thenThrow(new InternalServerException(SOMETHING_WENT_BADLY_WRONG));

        //Action & Assert
        mockMvc.perform(get("/v1/accounts/{accountNumber}", CUSTOMER_ACCOUNT)
                .accept(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(SERVER_ERROR))
                .andExpect(jsonPath("$.errorMessage").value(SOMETHING_WENT_BADLY_WRONG));
    }

    @SneakyThrows
    @Test
    void test_getAccountDetail_negative_case2() {
        //Assemble
        when(accountService.retrieve(anyLong())).thenThrow(new BadRequestException(SOMETHING_CLIENT_HAS_SENT_BAD));

        //Action & Assert
        mockMvc.perform(get("/v1/accounts/{accountNumber}", CUSTOMER_ACCOUNT).accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(CLIENT_ERROR))
                .andExpect(jsonPath("$.errorMessage").value(SOMETHING_CLIENT_HAS_SENT_BAD));
    }

    @SneakyThrows
    @Test
    void test_getAccountDetail_negative_case3() {
        //Assemble
        when(accountService.retrieve(anyLong())).thenThrow(new AccountNotFoundException(ACCOUNT_NOT_FOUND));

        //Action & Assert
        mockMvc.perform(get("/v1/accounts/{accountNumber}", CUSTOMER_ACCOUNT).accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(CLIENT_ERROR))
                .andExpect(jsonPath("$.errorMessage").value(ACCOUNT_NOT_FOUND));
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
    void test_getAccountDetail_negative_case5() {
        //Action & Assert
        mockMvc.perform(get("/v1/accounts/{accountNumber}", 12345).accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(CLIENT_ERROR))
                .andExpect(jsonPath("$.errorMessage", hasSize(1)))
        ;
    }

    @SneakyThrows
    @Test
    void test_createAccount_negative_case6() {
        //Action & Assert
        mockMvc.perform(post("/v1/accounts")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(asJsonString(AccountRequest.builder()
                        .build())
                ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(CLIENT_ERROR))
                .andExpect(jsonPath("$.errorMessage", hasSize(3)))
        ;
    }

    @SneakyThrows
    @Test
    void test_transferMoney_negative_case7() {
        //Action & Assert
        mockMvc.perform(post("/v1/transactions")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(asJsonString(MoneyTransactionRequest.builder()
                        .build())
                ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(CLIENT_ERROR))
                .andExpect(jsonPath("$.errorMessage", hasSize(3)))
        ;
    }

}
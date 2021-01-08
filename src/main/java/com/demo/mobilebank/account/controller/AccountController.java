package com.demo.mobilebank.account.controller;

import com.demo.mobilebank.account.bean.request.AccountRequest;
import com.demo.mobilebank.account.entity.Account;
import com.demo.mobilebank.account.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Customer Account APIs", description = "These APIs are used to create and retrieve customer accounts.")
public class AccountController {

    private final AccountService accountService;

    @GetMapping(value = "/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Account getAccountDetail(@PathVariable final String accountNumber) {
        log.debug("Account retrieval action.");
        return accountService.retrieve(accountNumber);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody final AccountRequest accountRequest) {
        log.debug("Account creation action.");
        return accountService.createAccount(accountRequest);
    }
}

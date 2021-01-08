package com.demo.mobilebank.account.bean.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorMessage {
    private final String errorCode;
    private final String errorMessage;
}

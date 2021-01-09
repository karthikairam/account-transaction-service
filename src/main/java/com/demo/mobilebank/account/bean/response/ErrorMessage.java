package com.demo.mobilebank.account.bean.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorMessage {
    private final String errorCode;
    private final List<String> errorMessage;
}

package com.demo.mobilebank.account.exception;

import com.demo.mobilebank.account.bean.response.ErrorMessage;

import java.util.List;

public interface StandardError {
    String getErrorCode();
    String getMessage();

    default ErrorMessage getErrorMessage() {
        return new ErrorMessage(getErrorCode(), List.of(getMessage()));
    }
}
